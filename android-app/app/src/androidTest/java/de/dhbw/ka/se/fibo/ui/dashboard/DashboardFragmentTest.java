package de.dhbw.ka.se.fibo.ui.dashboard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.mikephil.charting.charts.PieChart;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.MainActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

@RunWith(AndroidJUnit4.class)
public class DashboardFragmentTest {
    private final Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void beforeEach() {
        ApplicationState.getInstance(appContext).populateTestData();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("de.dhbw.ka.se.fibo", appContext.getPackageName());
    }

    @Test
    public void categoryFilterTestNotEveryCategorySelected() {
        // Change to the Dashboard view
        onView(withId(R.id.navigation_dashboard))
                .perform(click())
                .check(matches(isDisplayed()));

        // Integer that saves the current count of Entries in the Pie Chart before deselecting a category
        AtomicInteger pieEntryCount = new AtomicInteger();

        // Gets the context of the currently displayed activity and saves the current count of Entries in the Pie Chart
        activityScenarioRule.getScenario().onActivity(activity -> {
            pieEntryCount.set(((PieChart) activity.findViewById(R.id.dashboard_pieChart)).getData().getDataSet().getEntryCount());
        });

        // Click on the Filter button
        onView(withId(R.id.openFilterOptions))
                .perform(click());

        // Checks if the Filter panel has opened
        onView(hasSibling(withChild(withText(R.string.dashboard_filter_dialog_title))))
                .check(matches(isDisplayed()));

        // Gets the entry that belongs to Health and performs a click on it gets deselected
        onView(allOf(withClassName(Matchers.equalTo(AppCompatCheckedTextView.class.getName())), withText(R.string.HEALTH)))
                .perform(click());

        // Click on apply, to apply the deselection of Health
        onView(withText(R.string.apply))
                .perform(click());

        // Gets the new count of elements in the Pie Chart and checks if it's one less than the previous count
        activityScenarioRule.getScenario().onActivity(activity -> {
            int newPieEntryCount = ((PieChart) activity.findViewById(R.id.dashboard_pieChart)).getData().getDataSet().getEntryCount();
            assertEquals(newPieEntryCount, pieEntryCount.get() - 1);
        });
    }

    @Test
    public void categoryFilterTestWithoutCashflows() {
        ApplicationState.getInstance(appContext).setCashflows(new TreeSet<>());

        // Change to the Dashboard view
        onView(withId(R.id.navigation_dashboard))
                .perform(click())
                .check(matches(isDisplayed()));

        // Checks that there are no entries in the PieChart
        activityScenarioRule.getScenario().onActivity(activity -> {
            int newPieEntryCount = ((PieChart) activity.findViewById(R.id.dashboard_pieChart)).getData().getDataSet().getEntryCount();
            assertEquals(newPieEntryCount, 0);
        });

        // Click on the Filter button
        onView(withId(R.id.openFilterOptions))
                .perform(click());

        // Checks if the Filter panel has opened
        onView(hasSibling(withChild(withText(R.string.dashboard_filter_dialog_title))))
                .check(matches(isDisplayed()));

        // Checks that there are no entries in the Category field
        onView(allOf(withClassName(Matchers.equalTo(AppCompatCheckedTextView.class.getName()))))
                .check(doesNotExist());
    }

    @Test
    public void categoryFilterTestWithMultipleCashflowsForeSameCategory() {
        // Remove all Cashflows
        ApplicationState.getInstance(appContext).setCashflows(new TreeSet<>());

        // create Cashflows for testing
        Cashflow[] cashflows = new Cashflow[3];

        for (int i = 0; i < cashflows.length; i++) {
            cashflows[i] = new Cashflow(Category.CLOTHES,
                    CashflowType.EXPENSE,
                    BigDecimal.valueOf(10 + i),
                    LocalDateTime.of(2023, 4, 22, 14, 26 + i),
                    new Place("Adidas" + i, "Zara Straße"));
        }

        // Add Cashflows to the ApplicationContext
        for (Cashflow c : cashflows) {
            ApplicationState.getInstance(appContext).addCashflow(c);
        }

        // Change to the Dashboard view
        onView(withId(R.id.navigation_dashboard))
                .perform(click())
                .check(matches(isDisplayed()));

        // Click on the Filter button
        onView(withId(R.id.openFilterOptions))
                .perform(click());

        // Checks if the Filter panel has opened
        onView(hasSibling(withChild(withText(R.string.dashboard_filter_dialog_title))))
                .check(matches(isDisplayed()));

        // Checks if there is only the Clothes Entry in the Category list
        onView(allOf(withClassName(Matchers.equalTo(AppCompatCheckedTextView.class.getName())), withText(R.string.CLOTHES)))
                .check(matches(isChecked()));

        // Click on apply, close the dialog
        onView(withText(R.string.apply))
                .perform(click());

        // Checks if there is only one element in the Pie Chart
        activityScenarioRule.getScenario().onActivity(activity -> {
            int newPieEntryCount = ((PieChart) activity.findViewById(R.id.dashboard_pieChart)).getData().getDataSet().getEntryCount();
            assertEquals(newPieEntryCount, 1);
        });
    }

}