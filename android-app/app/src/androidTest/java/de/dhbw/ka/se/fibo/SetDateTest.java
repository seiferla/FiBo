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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SetDateTest {

    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    String thisMonthFirstDay = df.format(getThisMonthFirstDay());
    String nextMonthFirstDay = df.format(getNextMonthFirstDay());

    String dateTerm = "Datum"; // Set to @strings later
    String firstDate;


    Date getThisMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    Date getNextMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void setDateTest() {
        // Click on button that allows users to create a new cashflow
        ViewInteraction floatingActionButton = onView(withId(R.id.floatingButton));
        floatingActionButton.perform(click());

        // Check that no date has yet been entered
        ViewInteraction editText = onView(withId(R.id.date_text));
        editText.check(matches(withText("Datum")));

        // Click on date icon
        ViewInteraction checkableImageButton = onView(withId(com.google.android.material.R.id.text_input_end_icon));
        checkableImageButton.perform(click());

        // Select first of this month (previous date)
        DataInteraction materialTextView = onData(anything())
                .inAdapterView(withId(com.google.android.material.R.id.month_grid))
                .atPosition(5);
        materialTextView.perform(click());

        // https://copyprogramming.com/howto/recording-an-espresso-test-with-a-datepicker
        // https://developer.android.com/reference/androidx/test/espresso/contrib/PickerActions
        // https://www.lambdatest.com/automation-testing-advisor/kotlin/methods/android.support.test.espresso.contrib.PickerActions.setDate

        // Click on OK
        ViewInteraction materialButton3 = onView(withId(com.google.android.material.R.id.confirm_button));
        materialButton3.perform(click());

        // Check if date has been entered
        ViewInteraction editText2 = onView(withId(R.id.date_text));
        editText2.check(matches(withText("01.04.2023")));
        firstDate = String.valueOf(editText2);

        // Click on date icon again
        ViewInteraction checkableImageButton2 = onView(withId(com.google.android.material.R.id.text_input_end_icon));
        checkableImageButton2.perform(click());

        // Select next month
        ViewInteraction materialButton4 = onView(withId(com.google.android.material.R.id.month_navigation_next));
        materialButton4.perform(click());

        // Select first of next month (future date)
        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(withId(com.google.android.material.R.id.month_grid))
                .atPosition(0);
        materialTextView2.perform(click());

        // Click on OK
        ViewInteraction materialButton5 = onView(withId(com.google.android.material.R.id.confirm_button));
        materialButton5.perform(click());

        // Check if date field still says previous date (last month)
        ViewInteraction editText3 = onView(withId(R.id.date_text));
        editText3.check(matches(withText("01.04.2023")));
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
