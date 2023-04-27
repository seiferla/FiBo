package de.dhbw.ka.se.fibo.createAccount;


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
import static org.junit.Assert.assertNotNull;
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.dhbw.ka.se.fibo.CreateAccountActivity;
import de.dhbw.ka.se.fibo.R;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;


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
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field))));

        onView(withId(R.id.create_account_password_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty))));

    }

    @Test
    public void testEmailInput() {

        onView(withId(R.id.create_account_email))
                .perform(typeText("test"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_password_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty))));

    }

    @Test
    public void testPasswordWithoutInput() {

        onView(withId(R.id.create_account_password))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());


        onView(withId(R.id.create_account_password_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty))));

        onView(withId(R.id.create_account_email_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field))));

    }

    @Test
    public void testPasswordWithInput() {

        onView(withId(R.id.create_account_password))
                .perform(typeText("AsdfJklo1.2"), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        onView(withId(R.id.create_account_email_layer))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field))));

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
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_email_field)))));

        onView(withId(R.id.create_account_password_layer))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_password_field_empty)))));


    }


    @Test
    public void testHttpRequestWithValidCredentials() throws InterruptedException {
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
    public void testHttpRequestWithInvalidCredentials() throws InterruptedException {
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


    @Test
    public void testPageSwapFromCreateAccountToLogin() {
        onView(withId(R.id.click_here_for_login_text))
                .perform(click());
        onView(withId(R.id.login_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.login_layout))
                .check(matches(isDisplayed()));
    }

    /**
     * This test is known to be flaky.
     */
    @Test
    public void testNotAllowingBackAfterCreateAccount() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(200));

        String email = "fibo@fibo.de";
        String password = "test";

        onView(withId(R.id.create_account_email))
                .perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.create_account_password))
                .perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);

        // do some checks to increase the likelihood the UI changes
        // and we get redirected because of the successful login
        assertNotNull(request);
        assertNotNull(request.getRequestUrl());
        assertEquals(request.getRequestUrl().encodedPath(), "/users/register/");

        Map<String, List<String>> bodyString = getBodyString(request.getBody());
        assertEquals(bodyString.get("email"), List.of(email));
        assertEquals(bodyString.get("password"), List.of(password));

        // The following line is flaky!
        // especially on CI… it seems that there are racing conflicts
        onView(withId(R.id.floatingButton))
                .check(matches(isDisplayed()));

        Espresso.pressBackUnconditionally();
        assertEquals(Lifecycle.State.DESTROYED, activityScenarioRule.getScenario().getState());
    }

    private Map<String, List<String>> getBodyString(Buffer he) {
        Map<String, List<String>> parameters = new HashMap<>();

        String query;
        try (InputStream body = he.getBuffer().inputStream()) {
            query = new String(body.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] keyValuePairs = query.split("&");
        for (String keyValuePair : keyValuePairs) {
            String[] keyAndValue = keyValuePair.split("=", 2);

            String key = keyAndValue[0];
            String value = keyAndValue.length > 1 ? keyAndValue[1] : "";

            key = URLDecoder.decode(key, StandardCharsets.UTF_8);
            value = URLDecoder.decode(value, StandardCharsets.UTF_8);

            parameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        return parameters;
    }
}
