package de.dhbw.ka.se.fibo.filter.cashflow;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;

public class StartTimeFilter implements CashflowFilter {
    private LocalDate startTime;

    public StartTimeFilter(LocalDate startTime) {
        this.startTime = startTime;
    }

    @Override
    public Predicate<Cashflow> getPredicate() {
        return cashflow -> startTime.minusDays(1).isBefore(ChronoLocalDate.from(cashflow.getTimestamp()));
    }
}
