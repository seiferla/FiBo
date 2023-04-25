package de.dhbw.ka.se.fibo.strategies;

import androidx.appcompat.app.AppCompatActivity;

public interface LoginStrategy {
    void authenticate(AppCompatActivity activity, String email, String password);
}
