package de.dhbw.ka.se.fibo.createAccount;


import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.IsNot.not;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.dhbw.ka.se.fibo.CreateAccountActivity;
import de.dhbw.ka.se.fibo.R;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


public class CreateAccountTest {

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<CreateAccountActivity> activityScenarioRule
            = new ActivityScenarioRule<>(CreateAccountActivity.class);

    private MockWebServer server = new MockWebServer();

    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


    @Before
    public void setUp() throws IOException {
        server.start(8000);
    }

    @After
    public void tearDown() throws IOException {
        server.close();
    }

    @Test
    public void testCreateAccountButtonClick() {

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_email_layer))
                .check(matches(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.email_field))));

        onView(withId(R.id.create_account_password_layer))
                .check(matches(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.password_field))));

    }

    @Test
    public void testEmailInput() {

        onView(withId(R.id.create_account_email))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_password_layer))
                .check(matches(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.password_field))));

    }

    @Test
    public void testPasswordWithoutInput() {

        onView(withId(R.id.create_account_password))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());


        onView(withId(R.id.create_account_password_layer))
                .check(matches(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.password_field))));

        onView(withId(R.id.create_account_email_layer))
                .check(matches(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.email_field))));

    }

    @Test
    public void testPasswordWithInput() {

        onView(withId(R.id.create_account_password))
                .perform(typeText("AsdfJklo1.2"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_email_layer))
                .check(matches(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.email_field))));

    }

    @Test
    public void testValidInput() {

        onView(withId(R.id.create_account_email))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_email_layer))
                .check(matches(not(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.email_field)))));

        onView(withId(R.id.create_account_password_layer))
                .check(matches(not(CreateAccountTest.hasTextInputLayoutErrorText(appContext.getString(R.string.password_field)))));


    }


    @Test
    public void testHttpRequestWithValidCredentials() throws InterruptedException, IOException {
        server.enqueue(new MockResponse().setResponseCode(200));

        onView(withId(R.id.create_account_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.create_account_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testHttpRequestWithInvalidCredentials() throws InterruptedException, IOException {
        server.enqueue(new MockResponse().setResponseCode(500));

        onView(withId(R.id.create_account_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.create_account_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(doesNotExist());
    }

    public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(org.hamcrest.Description description) {

            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

        };
    }

}
