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

    @Rule
    public ActivityScenarioRule<SplashActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void setDateTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingButton), withContentDescription("Add Cashflow"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment_activity_main),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.date_text), withText("Datum"),
                        withParent(withParent(withId(R.id.date_layout))),
                        isDisplayed()));
        editText.check(matches(withText("Datum")));

        ViewInteraction checkableImageButton = onView(
                allOf(withId(com.google.android.material.R.id.text_input_end_icon),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.EndCompoundLayout")),
                                        1),
                                0),
                        isDisplayed()));
        checkableImageButton.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(com.google.android.material.R.id.month_navigation_previous), withContentDescription("Change to previous month"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.month_navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.month_grid),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(7);
        materialTextView.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(com.google.android.material.R.id.confirm_button), withText("OK"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.date_picker_actions),
                                        childAtPosition(
                                                withId(com.google.android.material.R.id.mtrl_calendar_main_pane),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.date_text), withText("06.03.2023"),
                        withParent(withParent(withId(R.id.date_layout))),
                        isDisplayed()));
        editText2.check(matches(withText("06.03.2023")));

        ViewInteraction checkableImageButton2 = onView(
                allOf(withId(com.google.android.material.R.id.text_input_end_icon),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.EndCompoundLayout")),
                                        1),
                                0),
                        isDisplayed()));
        checkableImageButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(com.google.android.material.R.id.month_navigation_next), withContentDescription("Change to next month"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.month_navigation_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(allOf(withId(com.google.android.material.R.id.month_grid),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(7);
        materialTextView2.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(com.google.android.material.R.id.confirm_button), withText("OK"),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.date_picker_actions),
                                        childAtPosition(
                                                withId(com.google.android.material.R.id.mtrl_calendar_main_pane),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.date_text), withText("06.03.2023"),
                        withParent(withParent(withId(R.id.date_layout))),
                        isDisplayed()));
        editText3.check(matches(withText("06.03.2023")));
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
