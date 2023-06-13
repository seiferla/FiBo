package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.dhbw.ka.se.fibo.R;

public class DatePickerAddingFragmentTest extends AddingFragmentTest {
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
}
