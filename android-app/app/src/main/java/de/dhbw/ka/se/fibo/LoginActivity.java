package de.dhbw.ka.se.fibo;

import static android.content.ContentValues.TAG;
import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.dhbw.ka.se.fibo.databinding.ActivityLoginBinding;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton loginButton;
    private TextView clickHereToRegisterText;
    private TextInputLayout loginPassword;
    private TextInputLayout loginEmail;
    Map<TextInputLayout, String> fieldsToBeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButton = binding.loginButton;
        clickHereToRegisterText = binding.clickHereToRegisterText;
        loginPassword = binding.loginPasswordLayer;
        loginEmail = binding.loginEmailLayer;

        //Define which fields are mandatory for the login
        fieldsToBeChecked = new HashMap<>();
        fieldsToBeChecked.put(loginEmail, getString(R.string.error_message_email_field));
        fieldsToBeChecked.put(loginPassword, getString(R.string.error_message_password_field_empty));

        Locale locale = Locale.GERMANY;
        Locale.setDefault(locale);
        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        getApplicationContext().createConfigurationContext(configuration);

        Helpers.updateSupportActionBarText(
                getApplicationContext(),
                getSupportActionBar(),
                getText(R.string.login_title)
        );

        initializeButtons();
    }


    private void initializeButtons() {
        loginButton.setOnClickListener(e -> loginButtonClicked());
        clickHereToRegisterText.setOnClickListener(e -> ActivityUtils.swapActivity(this, CreateAccountActivity.class, false));
    }

    private void loginButtonClicked() {
        if (!ActivityUtils.checkValidInput(fieldsToBeChecked)) {
            return;
        }
        JsonRequest<LoginResponse> request = buildLoginRequest();
        SharedVolleyRequestQueue
                .getInstance(getApplicationContext())
                .addToRequestQueue(request);
    }

    private JsonRequest<LoginResponse> buildLoginRequest() {
        String url = "/users/login/";
        String password = ActivityUtils.getTextInputLayoutFieldValue(loginPassword);
        String email = ActivityUtils.getTextInputLayoutFieldValue(loginEmail);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", email);
        requestParams.put("password", password);

        Response.Listener<LoginResponse> onSuccess = buildLoginSuccessListener();
        Response.ErrorListener onError = buildLoginErrorListener();
        return createAPIJSONRequest(LoginResponse.class, url, Request.Method.POST, requestParams, onSuccess, onError);
    }

    private Response.ErrorListener buildLoginErrorListener() {
        return error -> {
            if (null == error.networkResponse) {
                Toast.makeText(this, R.string.login_currently_unavailable, Toast.LENGTH_LONG)
                        .show();
                Log.e(TAG, String.valueOf(error));
                return;
            }

            String errorText = handleRegisterErrorCodes(error);

            Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
            Log.e(TAG, String.valueOf(error));
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
        return getString(errorMessage);
    }

    private Response.Listener<LoginResponse> buildLoginSuccessListener() {
        return response -> {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG)
                    .show();
            Log.i(TAG, "refresh: " + response.refresh);
            Log.i(TAG, "access: " + response.access);
            ActivityUtils.swapActivity(this, MainActivity.class, false);
        };
    }

    public static class LoginResponse {
        String refresh;
        String access;

        public LoginResponse(String refresh, String access) {
            this.refresh = refresh;
            this.access = access;
        }
    }


}