package de.dhbw.ka.se.fibo.login;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.dhbw.ka.se.fibo.LoginActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


public class LoginActivityTest {

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

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
    public void testLoginButtonClick() {

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.login_email_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field))));

        onView(withId(R.id.login_password_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty))));

    }

    @Test
    public void testEmailInput() {

        onView(withId(R.id.login_email))
                .perform(typeText("some@Email.de"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.login_password_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty))));

    }

    @Test
    public void testPasswordWithoutInput() {

        onView(withId(R.id.login_password))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());


        onView(withId(R.id.login_password_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty))));

        onView(withId(R.id.login_email_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field))));

    }

    @Test
    public void testPasswordWithInput() {

        onView(withId(R.id.login_password))
                .perform(typeText("AsdfJklo1.2"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.login_email_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field))));

    }

    @Test
    public void testValidInput() {

        onView(withId(R.id.login_email))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.login_email_layer))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field)))));

        onView(withId(R.id.login_password_layer))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty)))));


    }


    @Test
    public void testHttpRequestWithValidCredentials() throws InterruptedException {
        LoginStrategyProduction.LoginResponse loginResponse = new LoginStrategyProduction.LoginResponse("someJWTRefreshToken", "someJWTAccessToken");
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new Gson()
                        .toJson(loginResponse)
                ));

        onView(withId(R.id.login_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(matches(isDisplayed()));
    }


    @Test
    public void testHttpRequestWithInvalidCredentials() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(500));

        onView(withId(R.id.login_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(doesNotExist());
    }

    @Test
    public void testHttpRequestWithInvalidCredentialsBadRequest() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(400));

        onView(withId(R.id.login_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(doesNotExist());
    }

    @Test
    public void testHttpRequestWithInvalidCredentialsUnauthorized() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(401));

        onView(withId(R.id.login_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(doesNotExist());
    }


    @Test
    public void testPageSwapFromLoginToRegister() {
        onView(withId(R.id.click_here_to_register_text))
                .perform(click());
        onView(withId(R.id.create_account_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.create_account_layer))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNotAllowingBackAfterLogin() throws InterruptedException {
        LoginStrategyProduction.LoginResponse loginResponse = new LoginStrategyProduction.LoginResponse("someJWTRefreshToken", "someJWTAccessToken");
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new Gson()
                        .toJson(loginResponse)
                ));

        onView(withId(R.id.login_email))
                .perform(typeText("fibo@fibo.de"), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(matches(isDisplayed()));

        Espresso.pressBackUnconditionally();
        assertEquals(Lifecycle.State.DESTROYED, activityScenarioRule.getScenario().getState());
    }

}
