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

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {
    private final Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void beforeEach() {
        ApplicationState.getInstance(appContext).populateTestData();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("de.dhbw.ka.se.fibo", appContext.getPackageName());
    }

    @Test
    public void deleteUserCancelTest() {
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

        // Click on the Cancel Button
        onView(withText(R.string.cancelButton))
                .perform(click());

        // Checks that the user is back at the settings tab
        onView(withId((R.id.navigation_settings)))
                .check(matches(isDisplayed()));
    }
}