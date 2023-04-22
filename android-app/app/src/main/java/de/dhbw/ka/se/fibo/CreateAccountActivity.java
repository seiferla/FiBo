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
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.dhbw.ka.se.fibo.databinding.CreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountBinding binding;

    private MaterialButton registerButton;

    private RequestQueue requestQueue;


    private TextInputLayout passwordField;

    private TextInputLayout emailField;

    private String url = "http://10.0.2.2:8000/users/register/";


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
        passwordField = Objects.requireNonNull(binding.createAccountPasswordLayer);
        emailField = Objects.requireNonNull(binding.createAccountEmailLayer);
        String password = Objects.requireNonNull(binding.createAccountPassword.getText()).toString();
        String email = Objects.requireNonNull(binding.createAccountEmail.getText()).toString();
        createUser(email, password);

    }

    private void createUser(String email, String password) {


        if (!checkValidInput(email, password)) {
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

            Toast successToast = Toast.makeText(this, "Success", Toast.LENGTH_LONG);
            successToast.setGravity(Gravity.TOP, 0, 0);
            successToast.show();
            System.out.println("Successfully" + response);
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
            } else {
                Toast errorToast = Toast.makeText(this, "Login currently unavailable", Toast.LENGTH_LONG);
                errorToast.show();
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
        Singleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    private boolean checkValidInput(String email, String password) {

        boolean valid = true;

        emailField.setError(null);
        passwordField.setError(null);

        if (TextUtils.isEmpty(email)) {
            emailField.setError(getString(R.string.email_field));
            emailField.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError(getString(R.string.password_field));
            passwordField.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            valid = false;
        }

        return valid;
    }
}