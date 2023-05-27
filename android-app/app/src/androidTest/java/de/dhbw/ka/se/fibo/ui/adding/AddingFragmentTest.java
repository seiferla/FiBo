package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.TestMatchers;

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
    public void testBothPickers() {

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.confirm_button))
                .perform(click());

        onView(withId(com.google.android.material.R.id.material_timepicker_ok_button))
                .perform(click());

    }

    @Test
    public void testDatePickerCancelTimePicker() {

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.confirm_button))
                .perform(click());

        onView(withId(com.google.android.material.R.id.material_timepicker_cancel_button))
                .perform(click());

    }

    @Test
    public void testSetDatePickerCancelTimePicker() {

        String date = "24.05.2023";

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle))
                .perform(click());

        onView(allOf(TestMatchers.childAtPosition(TestMatchers.childAtPosition(withId(com.google.android.material.R.id.mtrl_picker_text_input_date),
                0), 0), isDisplayed()))
                .perform(replaceText(date), closeSoftKeyboard());

        onView(allOf(withId(com.google.android.material.R.id.confirm_button), isDisplayed()))
                .perform(click());

        onView(withId(com.google.android.material.R.id.material_timepicker_cancel_button))
                .perform(click());

        onView(withId(R.id.date_text)).check(matches(withText(date)));


    }


    @Test
    public void testDatePickerTimePicker() {

        String date = "24.05.2023, 16:50 Uhr";

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle))
                .perform(click());

        onView(allOf(TestMatchers.childAtPosition(TestMatchers.childAtPosition(withId(com.google.android.material.R.id.mtrl_picker_text_input_date),
                0), 0), isDisplayed()))
                .perform(replaceText(date), ViewActions.closeSoftKeyboard());

        onView(allOf(withId(com.google.android.material.R.id.confirm_button), isDisplayed()))
                .perform(click());

        onView(allOf(TestMatchers.childAtPosition(TestMatchers.childAtPosition(withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                0), 0), isDisplayed()))
                .perform(replaceText("16"));

        onView(withId(com.google.android.material.R.id.material_minute_text_input)).perform(click());

        onView(allOf(TestMatchers.childAtPosition(TestMatchers.childAtPosition(withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                0), 0), isDisplayed()))
                .perform(replaceText("50"), closeSoftKeyboard());

        onView(allOf(withId(com.google.android.material.R.id.material_timepicker_ok_button)))
                .perform(click());

        onView(withId(R.id.date_text))
                .check(matches(withText(date)));


    }
    public static ViewAction clickIcon(boolean isEndIcon) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TextInputLayout.class);
            }

            @Override
            public String getDescription() {
                return "Clicks the end or start icon";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextInputLayout item = (TextInputLayout) view;
                // Reach in and find the icon view since we don't have a public API to get a reference to it
                CheckableImageButton iconView =
                        item.findViewById(isEndIcon ? com.google.android.material.R.id.text_input_end_icon : com.google.android.material.R.id.text_input_start_icon);
                iconView.performClick();
            }
        };
    }


}