package de.dhbw.ka.se.fibo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.dhbw.ka.se.fibo.databinding.ActivityLoginBinding;
import de.dhbw.ka.se.fibo.strategies.LoginStrategy;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyLocal;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton loginButton;
    private TextView clickHereToRegisterText;
    private TextInputLayout loginPassword;
    private TextInputLayout loginEmail;

    private Map<TextInputLayout, String> fieldsToBeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ApplicationState.getInstance(getApplicationContext()).isAuthenticated()) {
            ActivityUtils.swapActivity(this, MainActivity.class, false);
            return;
        }

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

        LoginStrategy loginStrategy;
        if (BuildConfig.DEBUG && !ActivityUtils.isEspressoTesting()) {
            loginStrategy = new LoginStrategyLocal();
        } else {
            loginStrategy = new LoginStrategyProduction();
        }

        String password = ActivityUtils.getTextInputLayoutFieldValue(loginPassword);
        String email = ActivityUtils.getTextInputLayoutFieldValue(loginEmail);

        loginStrategy.authenticate(this, password, email);
    }

}