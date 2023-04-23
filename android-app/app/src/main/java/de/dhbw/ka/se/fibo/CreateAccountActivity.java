package de.dhbw.ka.se.fibo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.dhbw.ka.se.fibo.databinding.CreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountBinding binding;

    private MaterialButton registerButton;



    private TextInputLayout passwordFieldLayout;

    private TextInputLayout emailFieldLayout;

    private String url = "/users/register/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerButton = binding.createAccountButton;

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

        initializeButton();
    }

    private void initializeButton() {
        registerButton.setOnClickListener(e -> registerButtonClicked());
    }

    private void registerButtonClicked() {
        passwordFieldLayout = Objects.requireNonNull(binding.createAccountPasswordLayer);
        emailFieldLayout = Objects.requireNonNull(binding.createAccountEmailLayer);
        String password = Objects.requireNonNull(binding.createAccountPassword.getText()).toString();
        String email = Objects.requireNonNull(binding.createAccountEmail.getText()).toString();
        createUser(email, password);

    }

    /**
     * send a http post request to the backend with given params
     * @param email entered by the user
     * @param password entered by the user
     */
    private void createUser(String email, String password) {


        if (!checkValidInput(email, password)) {
            return;
        }

        Log.i(TAG, "Enqueuing request");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApplicationState.getInstance(this).getApiBaseUrl() + url, response -> {

            Toast successToast = Toast.makeText(this, "Success", Toast.LENGTH_LONG);
            successToast.setGravity(Gravity.TOP, 0, 0);
            successToast.show();
            Log.i(TAG, "Successfully" + response);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        }, error -> {
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

                Toast errorToast = Toast.makeText(this, getApplicationContext().getText(R.string.register_user_error), Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, String.valueOf(error));
            } else {
                Toast errorToast = Toast.makeText(this, "Login currently unavailable", Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, String.valueOf(error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        SharedVolleyRequestQueue requestQueue = SharedVolleyRequestQueue.getInstance(this);
        requestQueue.getRequestQueue().addRequestEventListener((request, event) -> {
            Log.i(TAG, "Registration request changed status: " + event + " " + request);
        });
        requestQueue.addToRequestQueue(stringRequest);

    }


    /**
     * checks the layout if error is caused
     * @param email has to be checked
     * @param password has to be checked
     * @return true if the user input is not empty
     */
    private boolean checkValidInput(String email, String password) {

        boolean valid = true;

        emailFieldLayout.setError(null);
        passwordFieldLayout.setError(null);

        if (TextUtils.isEmpty(email)) {
            emailFieldLayout.setError(getString(R.string.email_field));
            emailFieldLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordFieldLayout.setError(getString(R.string.password_field));
            passwordFieldLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            valid = false;
        }

        return valid;
    }


}