package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.navigation.Navigation;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;

@RunWith(AndroidJUnit4.class)
public class AddingFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mActivityScenarioRule.getScenario()
                .onActivity(activity -> Navigation.findNavController(activity, R.id.floatingButton)
                        .navigate(R.id.action_navigation_home_to_navigation_adding));
    }

    @Test
    public void justSomeInteraction() {
        onView(withId(R.id.tabLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.store_text_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.store_text)).check(matches(isDisplayed()));
        onView(withId(R.id.store_text)).perform(typeText("123"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.category_text)).perform(click());
    }


}