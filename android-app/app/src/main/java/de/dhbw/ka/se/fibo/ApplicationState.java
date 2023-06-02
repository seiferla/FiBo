package de.dhbw.ka.se.fibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class ApplicationState {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private static ApplicationState instance;
    private static final String SHARED_PREFERENCES_AUTHORIZATION_NAME = "authorization";
    private static final String SHARED_PREFERENCES_AUTHORIZATION_ACCESS_TOKEN = "accessToken";
    private static final String SHARED_PREFERENCES_AUTHORIZATION_REFRESH_TOKEN = "refreshToken";
    private SortedSet<Cashflow> cashflows;

    private static final String TAG = "ApplicationState";

    private byte[] jwsSigningKey = BuildConfig.JWS_SIGNING_KEY.getBytes();

    private ApplicationState(Context context) {
        Log.i("FiBo", "ApplicationState is initializing…");

        this.context = context;

        cashflows = new TreeSet<>();
        populateTestData();
    }

    @VisibleForTesting
    public void populateTestData() {
        String sampleDataAddress = "Lorenzstraße 19, 76135 Karlsruhe";
        cashflows.clear();
        cashflows.add(new Cashflow(Category.RESTAURANT, CashflowType.EXPENSE, BigDecimal.valueOf(8.5), LocalDateTime.now(), new Place("dm", "Am dm-Platz 1")));
        cashflows.add(new Cashflow(Category.HEALTH, CashflowType.EXPENSE, BigDecimal.valueOf(10), LocalDateTime.now().minusDays(1), new Place("kaufland", "Kaufplatz")));
        cashflows.add(new Cashflow(Category.SOCIAL_LIFE, CashflowType.INCOME, BigDecimal.valueOf(5.5), LocalDateTime.now().minusDays(2), new Place("Fabian", "In da club street")));
        cashflows.add(new Cashflow(Category.CULTURE, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.CLOTHES, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.EDUCATION, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.GIFT, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.INSURANCE, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.LIVING, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.MOBILITY, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.OTHER, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
        cashflows.add(new Cashflow(Category.HOUSEHOLD, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", sampleDataAddress)));
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

    public void addCashflow(Cashflow cashFlow) {
        cashflows.add(cashFlow);
    }

    /**
     * Determines whether the login is needed because of non-existent or expired refresh token
     *
     * @return true if login is unnecessary and user should be forwarded immediately
     */
    public boolean isAuthenticated() {
        Optional<String> refreshToken = getRefreshToken();

        if (!refreshToken.isPresent()) {
            Log.i(ApplicationState.TAG, "isAuthenticated() -> not authenticated, no refresh token persisted");

            return false;
        }

        try {
            Jwt<?, Claims> claims = Jwts.parserBuilder().setSigningKey(
                    Keys.hmacShaKeyFor(jwsSigningKey)
            ).build().parseClaimsJws(refreshToken.get());

            Log.i(ApplicationState.TAG, "claims are " + claims);
        } catch (JwtException e) {
            Log.i(ApplicationState.TAG, "isAuthenticated() -> not authenticated, refresh token invalid", e);

            return false;
        }

        Log.i(ApplicationState.TAG, "isAuthenticated() -> is authenticated");

        return true;
    }

    public Optional<String> getAccessToken() {
        String accessToken = context
                .getSharedPreferences(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_NAME, Context.MODE_PRIVATE)
                .getString(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_ACCESS_TOKEN, null);

        if (null == accessToken) {
            return Optional.empty();
        }

        return Optional.of(accessToken);
    }

    public Optional<String> getRefreshToken() {
        String refreshToken = context
                .getSharedPreferences(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_NAME, Context.MODE_PRIVATE)
                .getString(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_REFRESH_TOKEN, null);

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
                .getSharedPreferences(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_REFRESH_TOKEN, response.refresh)
                .putString(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_ACCESS_TOKEN, response.access)
                .apply();
    }

    public void clearAuthorization() {
        context
                .getSharedPreferences(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_REFRESH_TOKEN)
                .remove(ApplicationState.SHARED_PREFERENCES_AUTHORIZATION_ACCESS_TOKEN)
                .apply();
    }

    @VisibleForTesting
    public void setJwsSigningKey(byte[] jwsSigningKey) {
        this.jwsSigningKey = jwsSigningKey;
    }
}
