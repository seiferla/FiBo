package de.dhbw.ka.se.fibo.ui.adding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static de.dhbw.ka.se.fibo.TestMatchers.hasTextInputLayoutErrorText;

import org.junit.Test;

import de.dhbw.ka.se.fibo.R;

public class ItemDialogAddingFragmentTest extends AddingFragmentTest {
    //Item Tests

    private static final String FIRST_ITEM_AMOUNT = "1.0";
    private static final String EXPECTED_FIRST_ITEM_AMOUNT = "1.0 Stk.";
    private static final String FIRST_ITEM_PRICE = "1";
    private static final String EXPECTED_FIRST_ITEM_PRICE = "1,00 €";
    private static final String FIRST_ITEM_NAME = "Erster Beispielartikel";
    private static final String EDITED_FIRST_ITEM_AMOUNT = "1.5";
    private static final String EXPECTED_EDITED_FIRST_ITEM_AMOUNT = "1.5 Stk.";
    private static final String EDITED_FIRST_ITEM_PRICE = "2.3123123";
    private static final String EXPECTED_EDITED_FIRST_ITEM_PRICE = "2,31 €";
    private static final String EDITED_FIRST_ITEM_NAME = "Bearbeiteter Beispielartikel";

    @Test
    public void testSuccessfulItemAdding() {
        onView(withId(R.id.add_item_button))
                .perform(scrollTo(), click());

        //interact with dialog
        onView(withId(R.id.adding_fragment_dialog_container))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adding_fragment_dialog_item_price))
                .perform(typeText(FIRST_ITEM_PRICE));
        onView(withId(R.id.adding_fragment_dialog_item_amount))
                .perform(typeText(FIRST_ITEM_AMOUNT));
        onView(withId(R.id.adding_fragment_dialog_item_name))
                .perform(typeText(FIRST_ITEM_NAME));
        onView(withId(R.id.adding_fragment_dialog_save_button))
                .perform(click());

        //check for item in recyclerview
        onView(withId(R.id.adding_items_row_item_name))
                .check(matches(withText(FIRST_ITEM_NAME)));
        onView(withId(R.id.adding_items_row_item_price))
                .check(matches(withText(EXPECTED_FIRST_ITEM_PRICE)));
        onView(withId(R.id.adding_items_row_item_amount))
                .check(matches(withText(EXPECTED_FIRST_ITEM_AMOUNT)));
    }

    @Test
    public void testInitialInvisibleRecyclerView() {
        //recyclerView has to be invisible with 0 items
        onView(withId(R.id.adding_fragment_recyclerview))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testSuccessfulItemDeletion() {
        //First create a single item
        testSuccessfulItemAdding();
        //reopen edit panel
        onView(withId(R.id.adding_items_row_layout_wrapper))
                .perform(click());

        //interact with dialog
        onView(withId(R.id.adding_fragment_dialog_heading_delete_button))
                .perform(click());

        //recyclerView has to be invisible with 0 items
        onView(withId(R.id.adding_fragment_recyclerview))
                .check(matches(not(isDisplayed())));

        //also the item does not exist
        onView(withId(R.id.adding_items_row_item_name))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.adding_items_row_item_price))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.adding_items_row_item_amount))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testSuccessfulItemEditing() {

        //first add an Item
        testSuccessfulItemAdding();

        //open edit panel
        onView(withId(R.id.adding_items_row_layout_wrapper))
                .perform(click());

        //saving edited item
        onView(withId(R.id.adding_fragment_dialog_container))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adding_fragment_dialog_item_price))
                .perform(replaceText(EDITED_FIRST_ITEM_PRICE));
        onView(withId(R.id.adding_fragment_dialog_item_amount))
                .perform(replaceText(EDITED_FIRST_ITEM_AMOUNT));
        onView(withId(R.id.adding_fragment_dialog_item_name))
                .perform(replaceText(EDITED_FIRST_ITEM_NAME));
        onView(withId(R.id.adding_fragment_dialog_save_button))
                .perform(click());

        //avoiding flaky test results
        //during the refresh process, there might exists two views with the same id
        onView(allOf(withId(R.id.adding_items_row_item_name), withText(EDITED_FIRST_ITEM_NAME)))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.adding_items_row_item_price), withText(EXPECTED_EDITED_FIRST_ITEM_PRICE)))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.adding_items_row_item_amount), withText(EXPECTED_EDITED_FIRST_ITEM_AMOUNT)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCanceledItemEditing() {

        //first add an Item
        testSuccessfulItemAdding();

        //open edit panel
        onView(withId(R.id.adding_items_row_layout_wrapper))
                .perform(click());

        //Test saving edited item
        onView(withId(R.id.adding_fragment_dialog_container))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adding_fragment_dialog_item_price))
                .perform(replaceText(EDITED_FIRST_ITEM_PRICE));
        onView(withId(R.id.adding_fragment_dialog_item_amount))
                .perform(replaceText(EDITED_FIRST_ITEM_AMOUNT));
        onView(withId(R.id.adding_fragment_dialog_item_name))
                .perform(replaceText(EDITED_FIRST_ITEM_NAME));
        onView(withId(R.id.adding_fragment_dialog_cancel_button))
                .perform(click());


        onView(withId(R.id.adding_items_row_item_name))
                .check(matches(withText(FIRST_ITEM_NAME)));
        onView(withId(R.id.adding_items_row_item_price))
                .check(matches(withText(EXPECTED_FIRST_ITEM_PRICE)));
        onView(withId(R.id.adding_items_row_item_amount))
                .check(matches(withText(EXPECTED_FIRST_ITEM_AMOUNT)));
    }

    @Test
    public void testCorrectInitialPrefilledData() {
        //first add an Item
        testSuccessfulItemAdding();

        //open edit panel with just edited list entry
        onView(allOf(withId(R.id.adding_items_row_layout_wrapper), withChild(withText(EXPECTED_FIRST_ITEM_AMOUNT))))
                .perform(click());

        onView(withId(R.id.adding_fragment_dialog_container))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adding_fragment_dialog_item_name))
                .check(matches(withText(FIRST_ITEM_NAME)));
        onView(withId(R.id.adding_fragment_dialog_item_price))
                .check(matches(withText(FIRST_ITEM_PRICE)));
        onView(withId(R.id.adding_fragment_dialog_item_amount))
                .check(matches(withText(FIRST_ITEM_AMOUNT)));
    }

    @Test
    public void testCorrectEditedPrefilledData() {
        //first add an item and successfully edit it
        testSuccessfulItemEditing();

        //open edit panel with just edited list entry
        onView(allOf(withId(R.id.adding_items_row_layout_wrapper), withChild(withText(EXPECTED_EDITED_FIRST_ITEM_AMOUNT))))
                .perform(click());

        onView(withId(R.id.adding_fragment_dialog_container))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adding_fragment_dialog_item_name))
                .check(matches(withText(EDITED_FIRST_ITEM_NAME)));
        onView(withId(R.id.adding_fragment_dialog_item_price))
                .check(matches(withText(EDITED_FIRST_ITEM_PRICE)));
        onView(withId(R.id.adding_fragment_dialog_item_amount))
                .check(matches(withText(EDITED_FIRST_ITEM_AMOUNT)));
    }

    @Test
    public void testEmptyInputItemAdding() {
        String invalidNameInput = "";

        onView(withId(R.id.add_item_button))
                .perform(scrollTo(), click());

        //interact with dialog
        onView(withId(R.id.adding_fragment_dialog_container))
                .check(matches(isDisplayed()));
        onView(withId(R.id.adding_fragment_dialog_item_name))
                .perform(typeText(invalidNameInput));
        onView(withId(R.id.adding_fragment_dialog_save_button))
                .perform(click());

        //check for error in dialog
        onView(withId(R.id.adding_fragment_dialog_item_name_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext
                        .getString(R.string.error_message_name_field))));
        onView(withId(R.id.adding_fragment_dialog_item_amount_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext
                        .getString(R.string.error_message_amount_field))));
        onView(withId(R.id.adding_fragment_dialog_item_price_layout))
                .check(matches(hasTextInputLayoutErrorText(appContext
                        .getString(R.string.error_message_price_field))));
    }

}
