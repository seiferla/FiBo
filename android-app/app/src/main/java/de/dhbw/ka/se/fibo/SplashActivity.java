package de.dhbw.ka.se.fibo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

@SuppressLint("CustomSplashScreen")
// We need a custom splash screen because we want to support Android versions below 11
public class SplashActivity extends AppCompatActivity {

  private Thread welcomeThread;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    ActionBar supportActionBar = getSupportActionBar();
    Objects.requireNonNull(supportActionBar);
    supportActionBar.hide();

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
            Thread.sleep(2000);
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            Intent i = new Intent(SplashActivity.this,
                MainActivity.class);
            startActivity(i);
            finish();
          }
        }
      };
      welcomeThread.start();
    }
  }
}
