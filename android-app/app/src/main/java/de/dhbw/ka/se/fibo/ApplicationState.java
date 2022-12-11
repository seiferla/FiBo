package de.dhbw.ka.se.fibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;

public class ApplicationState {

    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private static ApplicationState instance;
    private SortedSet<Cashflow> cashflows;


    private ApplicationState(Context context) {
        Log.i("FiBo", "ApplicationState is initializing…");

        this.context = context;

        cashflows = new TreeSet<>();
        cashflows.add(new Cashflow(Category.RESTAURANT, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), LocalDateTime.now(), "dm"));
        cashflows.add(new Cashflow(Category.HEALTH, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), LocalDateTime.now().minusDays(5), "lidl"));
        cashflows.add(new Cashflow(Category.SOCIALLIFE, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(2), "Club"));
        cashflows.add(new Cashflow(Category.CULTURE, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), LocalDateTime.now().minusDays(1), "ZKM"));
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
}
