package de.dhbw.ka.se.fibo.ui.settings;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.util.Log;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.SharedVolleyRequestQueue;
import de.dhbw.ka.se.fibo.TestHelper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

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

        // Change to the Settings view
        onView(withId(R.id.navigation_settings))
                .perform(click())
                .check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws IOException {
        server.close();
    }

    @Test
    public void deleteUserCancelTest() {
        // Click on the delete user button
        onView(withId(R.id.deleteUser))
                .perform(click());

        // Check if the confirmation Dialog has opened
        onView(hasSibling(withChild(withText(R.string.delete_user_confirmation_title))))
                .check(matches(isDisplayed()));

        // Click on the Cancel Button
        onView(withText(R.string.cancelButton))
                .perform(click());

        // Checks that the user is back at the settings tab
        onView(withId((R.id.navigation_settings)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void deleteUserAccessTokenValidTest() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200));

        // Change to the Settings view
        onView(withId(R.id.navigation_settings))
                .perform(click())
                .check(matches(isDisplayed()));

        // Click on the delete user button
        onView(withId(R.id.deleteUser))
                .perform(click());

        // Check if the confirmation Dialog has opened
        onView(hasSibling(withChild(withText(R.string.delete_user_confirmation_title))))
                .check(matches(isDisplayed()));

        // Click on the Confirm Button
        onView(withText(R.string.delete_user_confirm_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);
        Log.i("FiBo", "deleteUserRequest = " + request);

        TestHelper.checkDeleteUserRequest(request);

        // Checks that the user is back at the login screen
        onView(withId((R.id.login_layout)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void deleteUserAccessTokenInvalidTest() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(401));

        // Change to the Settings view
        onView(withId(R.id.navigation_settings))
                .perform(click())
                .check(matches(isDisplayed()));

        // Click on the delete user button
        onView(withId(R.id.deleteUser))
                .perform(click());

        // Check if the confirmation Dialog has opened
        onView(hasSibling(withChild(withText(R.string.delete_user_confirmation_title))))
                .check(matches(isDisplayed()));

        // Click on the Confirm Button
        onView(withText(R.string.delete_user_confirm_button))
                .perform(click());

        // Wait for the HTTP request to complete
        RecordedRequest request = server.takeRequest(30, TimeUnit.SECONDS);
        Log.i("FiBo", "deleteUserRequest = " + request);

        TestHelper.checkDeleteUserRequest(request);

        // Checks that the user is still at the setting tab
        onView(withId((R.id.navigation_settings)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void logoutTest() {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_settings), withContentDescription("Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.logout), withText("Ausloggen"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_activity_main),
                                        0),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.login_email), withText("E-Mail-Adresse"),
                        withParent(withParent(withId(R.id.login_email_layer))),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.login_password), withText("Passwort"),
                        withParent(withParent(withId(R.id.login_password_layer))),
                        isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.login_button), withText("Login"),
                        withParent(allOf(withId(R.id.login_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
    }
}