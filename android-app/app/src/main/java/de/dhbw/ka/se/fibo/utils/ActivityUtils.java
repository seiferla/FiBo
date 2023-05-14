package de.dhbw.ka.se.fibo.utils;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import de.dhbw.ka.se.fibo.BuildConfig;

/**
 * This class can be referenced from activities to use some shared methods
 */
public class ActivityUtils {
    /**
     * This method navigates from the start activity to the destination activity.
     *
     * @param currentActivity the current Activity
     * @param destination     the desired destination Activity of the Navigation as a Class object
     * @param allowBack       flag to allow the user to go back to the previous activity
     */
    public static void swapActivity(AppCompatActivity currentActivity, Class<? extends AppCompatActivity> destination, boolean allowBack) {
        // the order is important to make sure Espresso recognizes the right state
        if (!allowBack) {
            currentActivity.finish();
        }

        Intent i = new Intent(currentActivity, destination);
        currentActivity.startActivity(i);
    }

    /**
     * This method is used to receive the String value of a TextView field.
     *
     * @param field the TextView to extract data from
     * @return a String with the input value of the given field
     * @throws NullPointerException if the textInput is null
     * @see #getTextInputLayoutFieldValue(TextInputLayout)
     */
    public static String getFieldValue(TextView field) {
        return Objects.requireNonNull(field.getText()).toString();
    }

    /**
     * This method is used to receive the String value of a TextView inside a TextInputLayout.
     *
     * @param layout the layout which is wrapped around the TextView to extract data from
     * @return a String with the input value of the given field
     * @throws NullPointerException if the textInput is null
     * @see #getFieldValue(TextView)
     */
    public static String getTextInputLayoutFieldValue(TextInputLayout layout) {
        return ActivityUtils.getFieldValue(Objects.requireNonNull(layout.getEditText()));
    }

    /**
     * @param fieldsToBeChecked a Map of TextInputLayouts as Keys and errorMessage Strings as Values that will be shown underneath each TextInputLayout
     * @return true if every needed User input exists. Otherwise false
     */
    public static boolean checkValidInput(Map<? extends TextInputLayout, String> fieldsToBeChecked) {
        AtomicBoolean valid = new AtomicBoolean(true);

        fieldsToBeChecked.keySet()
                .forEach(field -> field.setError(null));

        fieldsToBeChecked.forEach((field, errorMessage) -> {
            if (TextUtils.isEmpty(ActivityUtils.getTextInputLayoutFieldValue(field))) {
                field.setError(errorMessage);
                field.setErrorTextColor(ColorStateList.valueOf(Color.RED));
                valid.set(false);
            }
        });

        return valid.get();
    }

    /**
     * Checks whether the app is currently in a espresso test scenario.
     * It does not distinguish between the emulator version or a real device so you may want to use it in combination with
     * <strong>BuildConfig.DEBUG</strong>
     *
     * @return true if espresso is currently testing.
     */
    public static boolean isEspressoTesting() {
        try {
            // This must be the name of any class that are inside a androidTest/ repository.
            // Do not use "androidx.test.espresso.Espresso", as the package of it
            // is included at runtime. This is needed to allow managing idle resources.
            Class.forName("de.dhbw.ka.se.fibo.createAccount.CreateAccountTest");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * @return whether the app should strive to contact the backend or whether to run stand-alone
     * @Generated
     */
    public static boolean shouldContactBackend() {
        if (ActivityUtils.isEspressoTesting()) {
            // always contact backend, as responses are mocked during UI tests
            // and are part of the stuff that should be tested
            return true;
        }

        // we use the debug switch to indicate whether the app should run
        // stand-alone. This way, we don't need to have a Backend server set up during app
        // development (i.e. debug mode enabled).
        return !BuildConfig.DEBUG;
    }

}
