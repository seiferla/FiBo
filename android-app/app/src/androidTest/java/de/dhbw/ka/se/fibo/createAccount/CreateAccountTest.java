package de.dhbw.ka.se.fibo.createAccount;


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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.core.IsNot.not;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;


import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.io.IOException;

import de.dhbw.ka.se.fibo.CreateAccountActivity;
import de.dhbw.ka.se.fibo.R;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;


public class CreateAccountTest {

    private final Context application = InstrumentationRegistry.getInstrumentation().getTargetContext();


    @Rule
    public ActivityScenarioRule<CreateAccountActivity> activityRule = new ActivityScenarioRule(CreateAccountActivity.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private MockWebServer server = new MockWebServer();

    @Before
    public void setUp() throws IOException {
        server.start(8000);
    }
    @After
    public void tearDown() throws IOException {
        server.shutdown();
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
    public void testValidInput() {

        server.enqueue(new MockResponse().setResponseCode(200));

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


    //wiremock
}
