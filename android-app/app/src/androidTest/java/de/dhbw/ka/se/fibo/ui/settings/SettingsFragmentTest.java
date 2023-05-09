package de.dhbw.ka.se.fibo.ui.settings;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
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
import java.util.Optional;
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
        // Navigate to settings fragment
        onView(withId(R.id.navigation_settings))
                .perform(click());

        // Click on logout button
        onView(withId(R.id.logout))
                .perform(click());

        // Test that clearAuthorization() works
        assertEquals(ApplicationState.getInstance(appContext).getAccessToken(), Optional.empty());
        assertEquals(ApplicationState.getInstance(appContext).getRefreshToken(), Optional.empty());
        assertFalse(ApplicationState.getInstance(appContext).isAuthenticated());

        // Check that email login field is displayed
        onView(withId(R.id.login_layout))
                .check(matches(isDisplayed()));

        // Check that clicking back does not navigate to settings fragment
        Espresso.pressBackUnconditionally();
        assertEquals(Lifecycle.State.DESTROYED, activityScenarioRule.getScenario().getState());
    }
}