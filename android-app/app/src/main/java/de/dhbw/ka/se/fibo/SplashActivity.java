package de.dhbw.ka.se.fibo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.idling.CountingIdlingResource;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
// We need a custom splash screen because we want to support Android versions below 11
public class SplashActivity extends AppCompatActivity {

    private static CountingIdlingResource mIdlingResource;
    private Thread welcomeThread;

    private static final String TAG = "SplashActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar supportActionBar = getSupportActionBar();
        Objects.requireNonNull(supportActionBar);
        supportActionBar.hide();

        try {
            ApplicationState.getInstance(getApplicationContext()).syncCashflows(result -> {
                // interrupt welcome thread to immediately show next page
                if (null != welcomeThread) {
                    welcomeThread.interrupt();
                }
            });
        } catch (IllegalStateException e) {
            Log.w(SplashActivity.TAG, "syncing during splash activity failed, ignoring", e);
        }
    }

    @VisibleForTesting
    @NonNull
    public static synchronized CountingIdlingResource getIdlingResource() {
        if (null == SplashActivity.mIdlingResource) {
            SplashActivity.mIdlingResource = new CountingIdlingResource("splash_activity_idling_resource_counter");
            SplashActivity.mIdlingResource.increment();
            Log.i(SplashActivity.TAG, "CountingIdlingResource() created");
        }
        return SplashActivity.mIdlingResource;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == welcomeThread) {
            welcomeThread = new Thread() {
                @Override
                public void run() {
                    try {
                        super.run();
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Log.i(SplashActivity.TAG, "welcomeThread was interrupted", e);
                    } finally {
                        Intent i = new Intent(SplashActivity.this,
                                LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            };
            welcomeThread.start();
        }
    }
}
