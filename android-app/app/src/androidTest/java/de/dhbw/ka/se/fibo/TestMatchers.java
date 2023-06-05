package de.dhbw.ka.se.fibo;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TestMatchers {

    public static Matcher<View> hasTextInputLayoutErrorText(String expectedErrorText) {
        return new TypeSafeMatcher<>() {

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Testing if the TextInputLayout contains the expected error text: ")
                        .appendText(expectedErrorText);
            }

            @Override
            public boolean matchesSafely(View view) {
                String hint = "";

                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (null != error) {
                    hint = error.toString();
                }

                return expectedErrorText.equals(hint);
            }

        };
    }
}
