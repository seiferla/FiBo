package de.dhbw.ka.se.fibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import de.dhbw.ka.se.fibo.utils.backend.BackendSynchronizationFactory;
import de.dhbw.ka.se.fibo.utils.backend.BackendSynchronizationResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class ApplicationState {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private static ApplicationState instance;
    private SortedSet<Cashflow> cashflows;
    private SortedSet<Category> categories;
    private SortedSet<Place> places;

    private final String TAG = "ApplicationState";

    private byte[] jwsSigningKey = BuildConfig.JWS_SIGNING_KEY.getBytes();

    private ApplicationState(Context context) {
        Log.i("FiBo", "ApplicationState is initializing…");

        this.context = context;

        cashflows = new TreeSet<>();
        categories = new TreeSet<>();
        places = new TreeSet<>();
    }

    @VisibleForTesting
    public void setCashflows(SortedSet<Cashflow> cashflows) {
        this.cashflows = cashflows;
    }

    public static ApplicationState getInstance(Context context) {
        if (null == ApplicationState.instance) {
            ApplicationState.instance = new ApplicationState(context);
        }
        return ApplicationState.instance;
    }

    public SortedSet<Cashflow> getCashflows() {
        Log.v("FiBo", "ApplicationState#getCashflows()");
        return cashflows;
    }

    public void addCashflow(Cashflow cashflow) {
        cashflows.add(cashflow);
        categories.add(cashflow.getCategory());
        places.add(cashflow.getPlace());
    }

    public SortedSet<Category> getCategories() {
        Log.v("FiBo", "ApplicationState#getCategories()");
        return categories;
    }

    /**
     * Determines whether the login is needed because of non-existent or expired refresh token
     *
     * @return true if login is unnecessary and user should be forwarded immediately
     */
    public boolean isAuthenticated() {
        Optional<String> refreshToken = getRefreshToken();

        if (!refreshToken.isPresent()) {
            Log.i(TAG, "isAuthenticated() -> not authenticated, no refresh token persisted");

            return false;
        }

        try {
            Jwt<?, Claims> claims = Jwts.parserBuilder().setSigningKey(
                    Keys.hmacShaKeyFor(jwsSigningKey)
            ).build().parseClaimsJws(refreshToken.get());

            Log.i(TAG, "claims are " + claims);
        } catch (JwtException e) {
            Log.i(TAG, "isAuthenticated() -> not authenticated, refresh token invalid", e);

            return false;
        }

        Log.i(TAG, "isAuthenticated() -> is authenticated");

        return true;
    }

    public Optional<String> getAccessToken() {
        String accessToken = context
                .getSharedPreferences("authorization", 0)
                .getString("accessToken", null);

        if (null == accessToken) {
            return Optional.empty();
        }

        return Optional.of(accessToken);
    }

    public Optional<String> getRefreshToken() {
        String refreshToken = context
                .getSharedPreferences("authorization", 0)
                .getString("refreshToken", null);

        if (null == refreshToken) {
            return Optional.empty();
        }

        return Optional.of(refreshToken);
    }

    /**
     * @param response given response from login with access and refresh token to save
     * @see <a href="https://stackoverflow.com/a/10163623/8496913">here</a> why we store it inside shared preferences
     */
    public void storeAuthorization(LoginStrategyProduction.LoginResponse response) {
        context
                .getSharedPreferences("authorization", 0)
                .edit()
                .putString("refreshToken", response.refresh)
                .putString("accessToken", response.access)
                .commit(); // TODO: race condition during SplashSynchronizationTest?
        Log.i(TAG, "Authorization persisted");
    }

    public void clearAuthorization() {
        context
                .getSharedPreferences("authorization", 0)
                .edit()
                .remove("refreshToken")
                .remove("accessToken")
                .apply();
    }


    /**
     * This works by first polling cashflows, then categories and then mapping the data together.
     *
     * @param consumer if not null, the consumer is notified whenever there is a response ready
     */
    public void syncCashflows(Consumer<BackendSynchronizationResult> consumer) throws IllegalStateException {
        BackendSynchronizationFactory factory = new BackendSynchronizationFactory(context);
        factory.addResultListener(result -> {
            if (!result.wasSuccessful()) {
                Log.e(TAG, "backend synchronization wasn't successful!", result.getThrowable());
                return;
            }

            synchronized (this) {
                categories = result.getCategories();
                cashflows = result.getCashflows();
                places = result.getPlaces();
            }
        });

        if (null != consumer) {
            factory.addResultListener(consumer);
        }

        factory.startSynchronization();
    }

    @VisibleForTesting
    public void setJwsSigningKey(byte[] jwsSigningKey) {
        this.jwsSigningKey = jwsSigningKey;
    }
}
