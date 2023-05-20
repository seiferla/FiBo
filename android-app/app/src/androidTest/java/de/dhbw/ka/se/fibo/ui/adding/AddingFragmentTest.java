package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import android.content.Context;

import androidx.navigation.Navigation;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;

@RunWith(AndroidJUnit4.class)
public class AddingFragmentTest {
    private final Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

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
        onView(withId(R.id.store_text)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.category_text)).perform(click());
    }

    @Test
    public void testInvalidInput() {
        // click on the okay button without input, and test that every field has an error
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_store_field))));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_amount_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // change to the income tab and check that the source error is shown
        onView(withText(R.string.adding_income))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_source_field))));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_amount_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // add something to the source field and test that only the source error is gone
        onView(withId(R.id.store_text))
                .perform(scrollTo())
                .perform(typeText("Adidas Store"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_amount_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // change to the expense tab and check that the source error is gone as well
        onView(withText(R.string.adding_expense))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_amount_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // add something to the amount field and test that the the amount error is gone as well
        onView(withId(R.id.amount_text))
                .perform(scrollTo())
                .perform(typeText("10"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // select a date and test that the the date error is gone as well
        onView(withId(R.id.date_layout))
                .perform(scrollTo())
                .perform(click());
        // Checks if the Date panel has opened
        onView(hasSibling(withChild(withText(R.string.selectDate))))
                .check(matches(isDisplayed()));
        onView(withText(R.string.DatePickerPositiveButtonText))
                .perform(click());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // select a category and test that the the category error is gone as well
        onView(withId(R.id.category_text))
                .perform(scrollTo())
                .perform(click());
        // select the first category from list
        onData(Matchers.anything())
                .inRoot(RootMatchers.isPlatformPopup())
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));


        // add something to the address field and test that adding fragment has closed without errors
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText("Fibostraße 1"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        // Checks that the user is back at the settings tab
        onView(withId((R.id.navigation_home)))
                .check(matches(isDisplayed()));
    }
}