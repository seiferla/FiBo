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
import static org.hamcrest.Matchers.not;
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import androidx.test.espresso.matcher.RootMatchers;

import org.hamcrest.Matchers;
import org.junit.Test;

import de.dhbw.ka.se.fibo.R;

public class LayoutFieldsAddingFragmentTest extends AddingFragmentTest {

    private static final String TEST_ADDRESS_STRING = "Fibostraße 1";

    @Test
    public void testExpenseErrorsDisappearOnIncomeTabClick() {
        // click on the okay button without input
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        // check that error messages of text layouts are displayed
        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_store_field))));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        // click on income tab
        onView(withText(R.string.adding_income))
                .perform(scrollTo())
                .perform(click());

        // check that text layouts no longer have the error message
        onView(withId(R.id.store_text_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_store_field)))));
        onView(withId(R.id.amount_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field)))));
        onView(withId(R.id.date_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field)))));
        onView(withId(R.id.category_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field)))));
        onView(withId(R.id.address_text_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field)))));
    }

    @Test
    public void testIncomeErrorsDisappearOnExpenseTabClick() {
        // click on income tab
        onView(withText(R.string.adding_income))
                .perform(scrollTo())
                .perform(click());

        // click on the okay button without input
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        // check that error messages of text layouts are displayed
        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_source_field))));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        // click on expense tab
        onView(withText(R.string.adding_expense))
                .perform(scrollTo())
                .perform(click());

        // check that text layouts no longer have the error message
        onView(withId(R.id.store_text_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_source_field)))));
        onView(withId(R.id.amount_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field)))));
        onView(withId(R.id.date_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field)))));
        onView(withId(R.id.category_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field)))));
        onView(withId(R.id.address_text_layout))
                .check(matches(not(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field)))));
    }

    @Test
    public void testInvalidExpenseInput() {
        // click on the okay button without input, and test that every field has an error
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_store_field))));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        testInvalidSourceFieldInput();
        testInvalidAmountFieldInput();
        testInvalidInputDateFieldInput();
        testInvalidCategoryFieldInput();

        // add something to the address field and test that adding fragment has closed without errors
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText(TEST_ADDRESS_STRING), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());
        // Checks that the user is back at the home tab
        onView(withId((R.id.navigation_home)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidIncomeInput() {
        // change to the income tab and check that the errors are shown
        onView(withText(R.string.adding_income))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_source_field))));
        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        testInvalidSourceFieldInput();
        testInvalidAmountFieldInput();
        testInvalidInputDateFieldInput();
        testInvalidCategoryFieldInput();

        // add something to the address field and test that adding fragment has closed without errors
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText(TEST_ADDRESS_STRING), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        // Checks that the user is back at the home tab
        onView(withId((R.id.navigation_home)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidStoreFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_store_field))));

        // add something to the source field and test that the source error is gone
        onView(withId(R.id.store_text))
                .perform(scrollTo())
                .perform(typeText("Adidas Store"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

    @Test
    public void testInvalidSourceFieldInput() {
        onView(withText(R.string.adding_income))
                .perform(scrollTo())
                .perform(click());
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_source_field))));

        // add something to the source field and test that the source error is gone
        onView(withId(R.id.store_text))
                .perform(scrollTo())
                .perform(typeText("Adidas Store"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.store_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

    @Test
    public void testInvalidAmountFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_price_field))));

        // add something to the amount field and test that the the amount error is gone
        onView(withId(R.id.amount_text))
                .perform(scrollTo())
                .perform(typeText("10"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

    @Test
    public void testInvalidInputDateFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));

        // select a date and test that the the date error is gone
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

        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

    @Test
    public void testInvalidCategoryFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));

        // select a category and test that the category error is gone
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

        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

    @Test
    public void testInvalidAddressFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        // add something to the address field and test that the address error is gone
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText(TEST_ADDRESS_STRING), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

}
