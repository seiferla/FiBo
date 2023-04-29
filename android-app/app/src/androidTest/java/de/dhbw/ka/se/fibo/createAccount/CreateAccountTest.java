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
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.CreateAccountActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.SharedVolleyRequestQueue;
import de.dhbw.ka.se.fibo.TestHelper;
import de.dhbw.ka.se.fibo.strategies.LoginStrategyProduction;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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


    @BeforeClass
    public static void initResourceIdling() {
        IdlingRegistry.getInstance().register(
                SharedVolleyRequestQueue.getInstance(
                        InstrumentationRegistry.getInstrumentation().getTargetContext()
                ).getIdlingResource()
        );
    }

    @Before
    public void setUp() throws IOException {
        server.start(8000);
        ApplicationState.getInstance(appContext).clearAuthorization();
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

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        ApplicationState.getInstance(appContext).setJwsSigningKey(key.getEncoded());

        String refreshToken = Jwts.builder()
                .setClaims(Map.of(
                        "token_type", "refresh",
                        "user_id", 1
                ))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.UTC)))
                .signWith(key)
                .compact();

        LoginStrategyProduction.LoginResponse loginResponse = new LoginStrategyProduction.LoginResponse(refreshToken, "someJWTAccessToken");
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new Gson()
                        .toJson(loginResponse)
                ));

        String email = "fibo@fibo.de";
        String password = "test";

        onView(withId(R.id.create_account_email))
                .perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.create_account_password))
                .perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.create_account_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest registerRequest = server.takeRequest(30, TimeUnit.SECONDS);
        Log.i("FiBo", "registerRequest = " + registerRequest);

        RecordedRequest loginRequest = server.takeRequest(30, TimeUnit.SECONDS);
        Log.i("FiBo", "loginRequest = " + loginRequest);

        TestHelper.checkLoginRequestResponse(loginRequest, email, password);

        onView(withId(R.id.floatingButton))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testHttpRequestWithInvalidCredentials() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(500));

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

        TestHelper.checkRegisterRequestResponse(request, email);

        Log.i("FiBo", "request = " + request);

        onView(withId(R.id.floatingButton))
                .check(doesNotExist());
    }


    @Test
    public void testPageSwapFromCreateAccountToLogin() {
        onView(withId(R.id.click_here_for_login_text))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.login_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.login_layout))
                .check(matches(isDisplayed()));
    }

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

        TestHelper.checkRegisterRequestResponse(request, email);

        onView(withId(R.id.floatingButton))
                .check(matches(isDisplayed()));

        Espresso.pressBackUnconditionally();
        assertEquals(Lifecycle.State.DESTROYED, activityScenarioRule.getScenario().getState());
    }
}
