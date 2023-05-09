package de.dhbw.ka.se.fibo;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    private SortedSet<Cashflow> cashflows;

    private final String TAG = "ApplicationState";

    private byte[] jwsSigningKey = BuildConfig.JWS_SIGNING_KEY.getBytes();

    private ApplicationState(Context context) {
        Log.i("FiBo", "ApplicationState is initializing…");

        this.context = context;

        cashflows = new TreeSet<>();
        populateTestData();
    }

    @VisibleForTesting
    public void populateTestData() {
        cashflows.clear();
        cashflows.add(new Cashflow(new Category("HEALTH"), CashflowType.EXPENSE, BigDecimal.valueOf(8.5), LocalDateTime.now(), new Place("dm", "Am dm-Platz 1")));
        cashflows.add(new Cashflow(new Category("HEALTH"), CashflowType.EXPENSE, BigDecimal.valueOf(10), LocalDateTime.now().minusDays(1), new Place("kaufland", "Kaufplatz")));
        cashflows.add(new Cashflow(new Category("SOCIAL LIFE"), CashflowType.INCOME, BigDecimal.valueOf(5.5), LocalDateTime.now().minusDays(2), new Place("Fabian", "In da club street")));
        cashflows.add(new Cashflow(new Category("CULTURE"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("CLOTHES"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("EDUCATION"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("GIFT"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("INSURANCE"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("LIVING"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("MOBILITY"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("OTHER"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(new Category("HOUSEHOLD"), CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
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
        syncCashflows();
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

        if (accessToken == null) {
            return Optional.empty();
        }

        return Optional.of(accessToken);
    }

    public Optional<String> getRefreshToken() {
        String refreshToken = context
                .getSharedPreferences("authorization", 0)
                .getString("refreshToken", null);

        if (refreshToken == null) {
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
                .apply();
    }

    public void clearAuthorization() {
        context
                .getSharedPreferences("authorization", 0)
                .edit()
                .remove("refreshToken")
                .remove("accessToken")
                .apply();
    }


    private void syncCashflows() {
        Response.Listener<CashflowListResponse[]> onSuccess = response -> {

            Log.v(TAG, Arrays.toString(response));
        };
        Response.ErrorListener onError = error -> {
            if (error.networkResponse != null) {
                switch (error.networkResponse.statusCode) {
                    case 401:
                        Log.e(TAG, "Unauthorized");
                        break;
                    case 500:
                        Log.e(TAG, "Internal Server Error");
                        break;
                    default:
                        break;
                }

                Log.e(TAG, String.valueOf(error));
            } else {
                Log.e(TAG, String.valueOf(error));
            }
        };
        String url = "/cashflows/";
        JsonRequest<CashflowListResponse[]> jsonRequest = createAPIJSONRequest(CashflowListResponse[].class,url,
                Request.Method.GET,
                null,
                onSuccess,
                onError,
                this.getAccessToken());

        SharedVolleyRequestQueue requestQueue = SharedVolleyRequestQueue.getInstance(this.context);
        requestQueue.addToRequestQueue(jsonRequest);
    }




    @VisibleForTesting
    public void setJwsSigningKey(byte[] jwsSigningKey) {
        this.jwsSigningKey = jwsSigningKey;
    }
}
