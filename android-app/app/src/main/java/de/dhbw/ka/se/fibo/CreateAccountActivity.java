package de.dhbw.ka.se.fibo;

import static android.content.ContentValues.TAG;
import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIStringRequest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import de.dhbw.ka.se.fibo.databinding.CreateAccountBinding;
import de.dhbw.ka.se.fibo.strategies.LoginStrategy;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyLocal;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountBinding binding;

    private MaterialButton registerButton;

    private TextInputLayout passwordFieldLayout;

    private TextInputLayout emailFieldLayout;

    private TextView clickHereForLoginText;
    private Map<TextInputLayout, String> fieldsToBeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerButton = binding.createAccountButton;

        passwordFieldLayout = binding.createAccountPasswordLayer;
        emailFieldLayout = binding.createAccountEmailLayer;

        clickHereForLoginText = binding.clickHereForLoginText;

        //Define which fields are mandatory for the registration
        fieldsToBeChecked = new HashMap<>();
        fieldsToBeChecked.put(emailFieldLayout, getString(R.string.error_message_email_field));
        fieldsToBeChecked.put(passwordFieldLayout, getString(R.string.error_message_password_field_empty));

        Locale locale = Locale.GERMANY;
        Locale.setDefault(locale);
        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        getApplicationContext().createConfigurationContext(configuration);

        Helpers.updateSupportActionBarText(
                getApplicationContext(),
                getSupportActionBar(),
                getText(R.string.create_account_title)
        );

        initializeButtons();
    }

    private void initializeButtons() {
        registerButton.setOnClickListener(e -> registerButtonClicked());
        clickHereForLoginText.setOnClickListener(e -> ActivityUtils.swapActivity(this, LoginActivity.class, false));
    }

    private void registerButtonClicked() {
        registerButton.setClickable(false);
        String password = ActivityUtils.getFieldValue(binding.createAccountPassword);
        String email = ActivityUtils.getFieldValue(binding.createAccountEmail);
        createUser(email, password);
    }

    /**
     * send a http post request to the backend with given params
     *
     * @param email    entered by the user
     * @param password entered by the user
     */
    private void createUser(String email, String password) {
        if (!ActivityUtils.checkValidInput(fieldsToBeChecked)) {
            return;
        }

        Log.i(TAG, "Enqueuing request");

        Response.ErrorListener onError = error -> {
            if (error.networkResponse != null) {
                switch (error.networkResponse.statusCode) {
                    case 400:
                        Log.e(TAG, "Bad Request");
                        break;
                    case 401:
                        Log.e(TAG, "Unauthorized");
                        break;
                    case 404:
                        Log.e(TAG, "Not Found");
                        break;
                    case 500:
                        Log.e(TAG, "Internal Server Error");
                        break;
                    default:
                        break;
                }

                Toast errorToast = Toast.makeText(this, getApplicationContext().getText(R.string.invalid_input_error), Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, String.valueOf(error));
            } else {
                Toast errorToast = Toast.makeText(this, R.string.registration_currently_unavailable, Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, String.valueOf(error));
            }
            registerButton.setClickable(true);
        };

        Response.Listener<String> onSuccess = response -> {
            Toast successToast = Toast.makeText(this, "Success", Toast.LENGTH_LONG);
            successToast.setGravity(Gravity.TOP, 0, 0);
            successToast.show();

            LoginStrategy loginStrategy;
            if (BuildConfig.DEBUG && !ActivityUtils.isEspressoTesting()) {
                loginStrategy = new LoginStrategyLocal();
            } else {
                loginStrategy = new LoginStrategyProduction();
            }

            loginStrategy.authenticate(this, email, password);
        };

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        String url = "/users/register/";
        StringRequest stringRequest = createAPIStringRequest(url, Request.Method.POST, params, onSuccess, onError, Optional.empty());

        SharedVolleyRequestQueue requestQueue = SharedVolleyRequestQueue.getInstance(this);
        requestQueue.addToRequestQueue(stringRequest);
    }
}