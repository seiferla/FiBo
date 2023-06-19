package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.dhbw.ka.se.fibo.R;

public class DatePickerAddingFragmentTest extends AddingFragmentTest {

    private String date = "24.05.2023";

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy");

    private static DateTimeFormatter hoursFormat = DateTimeFormatter.ofPattern("HH");

    private static DateTimeFormatter minutesFormat = DateTimeFormatter.ofPattern("mm");

    private String currentHours = hoursFormat.format(LocalDateTime.now());

    private String currentMinutes = minutesFormat.format(LocalDateTime.now());

    private String currentDate = dateFormat.format(LocalDateTime.now());


    @Test
    public void testFutureDateNotPossible() {
        DateTimeFormatter shortFormat = DateTimeFormatter.ofPattern("dd.MM.yy");
        String today = shortFormat.format(LocalDateTime.now());
        String tomorrow = shortFormat.format(LocalDateTime.now().plusDays(1));

        // Click on date icon
        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        // Click on edit icon to enter new date
        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle))
                .perform(click());

        // Replace current date with the next day
        onView(withText(today))
                .perform(replaceText(tomorrow));

        // Close Keyboard
        closeSoftKeyboard();

        // Check that you can no longer click the confirm button
        onView(withId(com.google.android.material.R.id.confirm_button))
                .check(matches(isNotEnabled()));
    }
    public void testDatePickerCancelTimePicker() {

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.confirm_button))
                .perform(click());

        onView(withId(com.google.android.material.R.id.material_timepicker_cancel_button))
                .perform(click());

        onView(withId(R.id.date_text)).check(matches(not(withText(date))));

    }

    @Test
    public void testSetDatePickerCancelTimePicker() {

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle))
                .perform(click());

        onView(withText(currentDate))
                .perform(replaceText(date));

        onView(withText(date)).perform(closeSoftKeyboard());

        onView(allOf(withId(com.google.android.material.R.id.confirm_button), isDisplayed()))
                .perform(click());

        onView(withId(com.google.android.material.R.id.material_timepicker_cancel_button))
                .perform(click());

        onView(withId(R.id.date_text)).check(matches(withText(date)));


    }


    @Test
    public void testDatePickerTimePicker() {


        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle))
                .perform(click());

        onView(withText(currentDate))
                .perform(replaceText(date));

        onView(withText(date))
                .perform(closeSoftKeyboard());

        onView(withId(com.google.android.material.R.id.confirm_button))
                .perform(click());

        onView(withId(com.google.android.material.R.id.material_timepicker_ok_button))
                .perform(click());

        StringBuilder builder = new StringBuilder();
        builder.append(date + ", " + currentHours + ":" + currentMinutes + " Uhr");

        onView(withId(R.id.date_text)).check(matches(withText(builder.toString())));


    }


    public static ViewAction clickTextInputLayoutIcon(boolean isEndIcon) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextInputLayout.class);
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
