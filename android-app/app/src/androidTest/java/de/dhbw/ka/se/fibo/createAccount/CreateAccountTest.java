package de.dhbw.ka.se.fibo.createAccount;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.core.IsNot.not;

import android.content.Context;


import com.github.tomakehurst.wiremock.junit.WireMockRule;

import de.dhbw.ka.se.fibo.CreateAccountActivity;
import de.dhbw.ka.se.fibo.R;


public class CreateAccountTest {


    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8000);
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
    public void testValidInput() {

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




    public void testHttpRequest(){



    }


    //wiremock
}
