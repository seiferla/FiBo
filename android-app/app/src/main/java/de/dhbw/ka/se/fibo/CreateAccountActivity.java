package de.dhbw.ka.se.fibo;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.dhbw.ka.se.fibo.databinding.CreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountBinding binding;

    private MaterialButton registerButton;


    private TextInputEditText create_account_email;

    private TextInputEditText create_account_password;

    private TextInputLayout passwordField;

    private TextInputLayout emailField;

    private Intent i;


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
        i = new Intent(CreateAccountActivity.this,
                MainActivity.class);
        create_account_email = findViewById(R.id.create_account_email);
        String email = create_account_email.getText().toString();
        passwordField = findViewById(R.id.create_account_password_layer);
        emailField = findViewById(R.id.create_account_email_layer);
        create_account_password = findViewById(R.id.create_account_password);
        String password = create_account_password.getText().toString();
        createUser(email, password);

    }

    private void createUser(String email, String password) {
        String url = "http://10.0.2.2:8000/users/register/";

        RequestQueue queue = Volley.newRequestQueue(this);

        System.out.println("Email: " + email);
        System.out.println("Passwort: " + password);


        if (!checkValidInput(email, password)) {
            return;
        } else {

            try {
                StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

                    Toast successToast = Toast.makeText(this, "Success", Toast.LENGTH_LONG);
                    successToast.setGravity(Gravity.TOP, 0, 0);
                    successToast.show();
                    System.out.println("Successfully" + response);
                    startActivity(i);
                    finish();

                }, error -> {

                    if (error.networkResponse != null) {
                        switch (error.networkResponse.statusCode) {
                            case 400:
                                System.out.println("Bad Request");
                                break;
                            case 401:
                                System.out.println("Unauthorized");
                                break;
                            case 404:
                                System.out.println("Not Found");
                                break;
                            case 500:
                                System.out.println("Internal Server Error");
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
                queue.add(request);

            } catch (Exception e) {
                Toast errorToast = Toast.makeText(this, "Login is currently not available", Toast.LENGTH_LONG);
                errorToast.show();
            }

        }

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