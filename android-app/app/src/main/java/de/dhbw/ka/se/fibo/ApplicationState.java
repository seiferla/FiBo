package de.dhbw.ka.se.fibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

public class ApplicationState {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private static ApplicationState instance;
    private SortedSet<Cashflow> cashflows;
    private String apiBaseUrl;


    private ApplicationState(Context context) {
        Log.i("FiBo", "ApplicationState is initializing…");

        this.context = context;

        try {
            Class.forName("androidx.test.espresso.Espresso");
            apiBaseUrl = "http://localhost:8000";
        } catch (ClassNotFoundException e) {
            apiBaseUrl = "http://10.0.2.2:8000";
        }

        cashflows = new TreeSet<>();
        populateTestData();
    }

    @VisibleForTesting
    public void populateTestData() {
        cashflows.clear();
        cashflows.add(new Cashflow(Category.RESTAURANT, CashflowType.EXPENSE, BigDecimal.valueOf(8.5), LocalDateTime.now(), new Place("dm", "Am dm-Platz 1")));
        cashflows.add(new Cashflow(Category.HEALTH, CashflowType.EXPENSE, BigDecimal.valueOf(10), LocalDateTime.now().minusDays(1), new Place("kaufland", "Kaufplatz")));
        cashflows.add(new Cashflow(Category.SOCIAL_LIFE, CashflowType.INCOME, BigDecimal.valueOf(5.5), LocalDateTime.now().minusDays(2), new Place("Fabian", "In da club street")));
        cashflows.add(new Cashflow(Category.CULTURE, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.CLOTHES, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.EDUCATION, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.GIFT, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.INSURANCE, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.LIVING, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.MOBILITY, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.OTHER, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
        cashflows.add(new Cashflow(Category.HOUSEHOLD, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(5), new Place("ZKM", "Lorenzstraße 19, 76135 Karlsruhe")));
    }

    @VisibleForTesting
    public void setCashflows(SortedSet<Cashflow> cashflows) {
        this.cashflows = cashflows;
    }

    public static ApplicationState getInstance(Context context) {
        if (null == ApplicationState.instance) {
            ApplicationState.instance = new ApplicationState(context);
        }
        return ApplicationState.instance;
    }

    public SortedSet<Cashflow> getCashflows() {
        Log.v("FiBo", "ApplicationState#getCashflows()");

        return cashflows;
    }

    public void addCashflow(Cashflow cashFlow) {
        cashflows.add(cashFlow);
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
