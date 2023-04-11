package de.dhbw.ka.se.fibo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import de.dhbw.ka.se.fibo.databinding.ActivityMainBinding;
import de.dhbw.ka.se.fibo.databinding.CreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private CreateAccountBinding binding;

    private MaterialButton registerButton;

    private Thread buttonClickThread;

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

        //The part below is for navigationBar functionality (swapping pages)

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

        initializeButton();
    }

    private void initializeButton() {
        registerButton.setOnClickListener(e -> registerButtonClicked());
    }

    private void registerButtonClicked() {
        Intent i = new Intent(CreateAccountActivity.this,
                MainActivity.class);
        startActivity(i);
        finish();
    }
}