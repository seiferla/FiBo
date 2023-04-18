package de.dhbw.ka.se.fibo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.HttpAuthHandler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import de.dhbw.ka.se.fibo.databinding.CreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountBinding binding;
    private MaterialButton registerButton;

    private Thread buttonClickThread;

    private static final String post_url = "http://127.0.0.1:8000/users/register/";
    private TextInputEditText create_account_email;

    private TextInputEditText create_account_password;


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
        Intent i = new Intent(CreateAccountActivity.this,
                MainActivity.class);
        create_account_email = findViewById(R.id.create_account_email);
        String email = create_account_email.getText().toString();

        create_account_password = findViewById(R.id.create_account_password);
        String password = create_account_password.getText().toString();
        
        createUser(email, password);
        startActivity(i);
        finish();
    }

    private void createUser(String email, String password) {

        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}