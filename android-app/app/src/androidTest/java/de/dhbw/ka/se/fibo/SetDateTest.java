package de.dhbw.ka.se.fibo;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SetDateTest {

    String dateTerm; // Set to @strings later
    String firstDate;

    @Rule
    public ActivityScenarioRule<SplashActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void setDateTest() {
        // Click on button that allows users to create a new cashflow
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingButton), withContentDescription("Add Cashflow"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_activity_main),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Check that no date has yet been entered
        ViewInteraction editText = onView(
                allOf(withId(R.id.date_text), withText("Datum"),
                        withParent(withParent(withId(R.id.date_layout))),
                        isDisplayed()));
        editText.check(matches(withText(dateTerm)));

        // Click on date icon
        ViewInteraction checkableImageButton = onView(
                allOf(withId(com.google.android.material.R.id.text_input_end_icon),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.EndCompoundLayout")),
                                        1),
                                0),
                        isDisplayed()));
        checkableImageButton.perform(click());

        // Select first of this month (previous date)
        DataInteraction materialTextView = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.month_grid),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(5);
        materialTextView.perform(click());

        // Click on OK
        ViewInteraction materialButton3 = onView(
                allOf(withId(com.google.android.material.R.id.confirm_button), withText("OK"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.date_picker_actions),
                                        childAtPosition(
                                                withId(com.google.android.material.R.id.mtrl_calendar_main_pane),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        // Check if date has been entered
        ViewInteraction editText2 = onView(
                allOf(withId(R.id.date_text), // withText("01.04.2023"),
                        withParent(withParent(withId(R.id.date_layout))),
                        isDisplayed()));
        editText2.check(matches(withText("01.04.2023")));
        firstDate = String.valueOf(editText2);

        // Click on date icon again
        ViewInteraction checkableImageButton2 = onView(
                allOf(withId(com.google.android.material.R.id.text_input_end_icon),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.EndCompoundLayout")),
                                        1),
                                0),
                        isDisplayed()));
        checkableImageButton2.perform(click());

        // Select next month
        ViewInteraction materialButton4 = onView(
                allOf(withId(com.google.android.material.R.id.month_navigation_next), withContentDescription("Change to next month"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.month_navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        // Select first of next month (future date)
        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.month_grid),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(0);
        materialTextView2.perform(click());

        // Click on OK
        ViewInteraction materialButton5 = onView(
                allOf(withId(com.google.android.material.R.id.confirm_button), withText("OK"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.date_picker_actions),
                                        childAtPosition(
                                                withId(com.google.android.material.R.id.mtrl_calendar_main_pane),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        // Check if date field still says previous date (last month)
        ViewInteraction editText3 = onView(
                allOf(withId(R.id.date_text), // withText("01.04.2023"),
                        withParent(withParent(withId(R.id.date_layout))),
                        isDisplayed()));
        editText3.check(matches(withText(firstDate)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
