package de.dhbw.ka.se.fibo.strategies;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class LoginStrategyLocal implements LoginStrategy {

    @Override
    public void authenticate(AppCompatActivity activity, String email, String password) {
        Log.i(TAG, "LoginStrategyLocal - email: " + email);
        Log.i(TAG, "LoginStrategyLocal - password: " + password);
        ActivityUtils.swapActivity(activity, MainActivity.class, false);
    }
}
