package de.dhbw.ka.se.fibo.createAccount;


import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.IsNot.not;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import de.dhbw.ka.se.fibo.CreateAccountActivity;
import de.dhbw.ka.se.fibo.R;


public class CreateAccountTest {

    private static final int WIREMOCK_PORT = 8080;


    @Rule
    public ActivityScenarioRule<CreateAccountActivity> activityRule = new ActivityScenarioRule<>(CreateAccountActivity.class);

    @BeforeClass
    public static void setUpWireMockServer() {

    }

    @Test
    public void testCreateAccountButtonClick() {

        onView(withId(R.id.create_account_button))
                .perform(click());
    }

    @Test
    public void testEmailInput() {

        onView(withId(R.id.create_account_email))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());
    }

    @Test
    public void testPasswordWithoutInput() {

        onView(withId(R.id.create_account_password))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());
    }

    @Test
    public void testPasswordWithInput() {

        onView(withId(R.id.create_account_password))
                .perform(typeText("AsdfJklo1.2"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());
    }


    @Test
    public void testValidInput() throws InterruptedException {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        onView(withId(R.id.create_account_email))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_email_layer))
                .check(matches(not(hasErrorText(appContext.getString(R.string.email_field)))));

        onView(withId(R.id.create_account_password_layer))
                .check(matches(not(hasErrorText(appContext.getString(R.string.password_field)))));


    }


    @Test
    public void testHttpRequest() {

    }

    //wiremock
}
