package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import android.content.Context;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public void testInvalidExpenseInput() {
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

        TestInvalidSourceFieldInput();
        TestInvalidAmountFieldInput();
        TestInvalidInputDateFieldInput();
        TestInvalidCategoryFieldInput();

        // add something to the address field and test that adding fragment has closed without errors
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText("Fibostraße 1"), closeSoftKeyboard());

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
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_amount_field))));
        onView(withId(R.id.date_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_date_field))));
        onView(withId(R.id.category_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_category_field))));
        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        TestInvalidSourceFieldInput();
        TestInvalidAmountFieldInput();
        TestInvalidInputDateFieldInput();
        TestInvalidCategoryFieldInput();

        // add something to the address field and test that adding fragment has closed without errors
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText("Fibostraße 1"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        // Checks that the user is back at the home tab
        onView(withId((R.id.navigation_home)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void TestInvalidStoreFieldInput() {
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
    public void TestInvalidSourceFieldInput() {
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
    public void TestInvalidAmountFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_amount_field))));

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
    public void TestInvalidInputDateFieldInput() {
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
    public void TestInvalidCategoryFieldInput() {
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
    public void TestInvalidAddressFieldInput() {
        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext.getString(R.string.error_message_address_field))));

        // add something to the address field and test that the address error is gone
        onView(withId(R.id.address_text))
                .perform(scrollTo())
                .perform(typeText("Fibostraße 1"), closeSoftKeyboard());

        onView(withId(R.id.okayButton))
                .perform(scrollTo())
                .perform(click());

        onView(withId(R.id.address_text_layout))
                .check(matches(hasTextInputLayoutErrorText("")));
    }

    @Test
    public void testFutureDateNotPossible() {
        DateTimeFormatter full = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        String today_full = full.format(now);

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.confirm_button))
                .perform(click());

        onView(withId(R.id.date_text)).check(matches(withText(today_full)));

        onView(withId(R.id.date_layout))
                .perform(AddingFragmentTest.clickIcon(true));

        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle))
                .perform(click());

        onView(withText("30.05.23"))
                .perform(replaceText("31.12.2999"));

        onView(withText("31.12.2999"))
                .perform(closeSoftKeyboard());

        onView(withId(com.google.android.material.R.id.confirm_button))
                .check(matches(isNotEnabled()));
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