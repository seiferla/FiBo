package de.dhbw.ka.se.fibo.strategies;

import static android.content.ContentValues.TAG;
import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.SharedVolleyRequestQueue;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class LoginStrategyProduction implements LoginStrategy {
    private AppCompatActivity activity;

    @Override
    public void authenticate(AppCompatActivity activity, String email, String password) {
        this.activity = activity;
        JsonRequest<LoginResponse> request = buildLoginRequest(email, password);
        SharedVolleyRequestQueue
                .getInstance(this.activity.getApplicationContext())
                .addToRequestQueue(request);
    }


    private JsonRequest<LoginResponse> buildLoginRequest(String email, String password) {
        String url = "/users/login/";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", email);
        requestBody.put("password", password);

        Response.Listener<LoginResponse> onSuccess = buildLoginSuccessListener();
        Response.ErrorListener onError = buildLoginErrorListener();
        return createAPIJSONRequest(LoginResponse.class, url, Request.Method.POST, requestBody, onSuccess, onError, Optional.empty());
    }

    private Response.ErrorListener buildLoginErrorListener() {
        return error -> {
            if (null == error.networkResponse) {
                Toast.makeText(activity, R.string.login_currently_unavailable, Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, String.valueOf(error), error.getCause());
                return;
            }

            String errorText = handleRegisterErrorCodes(error);

            Toast.makeText(activity, errorText, Toast.LENGTH_LONG).show();

            Log.e(TAG, String.valueOf(error), error.getCause());
            Log.e(TAG, "Response was " + new String(error.networkResponse.data));
        };
    }

    private String handleRegisterErrorCodes(VolleyError error) {
        int errorMessage;
        switch (error.networkResponse.statusCode) {
            case 400:
                Log.e(TAG, "Bad Request");
                errorMessage = R.string.error_message_invalid_credentials;
                break;
            case 401:
                Log.e(TAG, "Unauthorized");
                errorMessage = R.string.error_message_invalid_credentials;
                break;
            case 500:
                Log.e(TAG, "Internal Server Error");
                errorMessage = R.string.error_message_server_error;
                break;
            default:
                Log.e(TAG, "Internal Server Error");
                errorMessage = R.string.error_message_unknown_error_occurred;
        }
        return activity.getString(errorMessage);
    }

    private Response.Listener<LoginResponse> buildLoginSuccessListener() {
        return response -> {
            Toast.makeText(activity, "Success", Toast.LENGTH_LONG)
                    .show();
            Log.i(TAG, "refresh: " + response.refresh);
            Log.i(TAG, "access: " + response.access);
            ActivityUtils.swapActivity(activity, MainActivity.class, false);

            ApplicationState.getInstance(activity.getApplicationContext()).storeAuthorization(response);
        };
    }

    public static class LoginResponse {
        public String refresh;
        public String access;

        public LoginResponse(String refresh, String access) {
            this.refresh = refresh;
            this.access = access;
        }
    }


}
