package de.dhbw.ka.se.fibo.filter.cashflow;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;

public class EndTimeFilter implements CashflowFilter{
    private LocalDate endTime;

    public EndTimeFilter(LocalDate endTime) {
        this.endTime = endTime;
    }

    @Override
    public Predicate<Cashflow> getPredicate() {
        return new Predicate<Cashflow>() {
            @Override
            public boolean test(Cashflow cashflow) {
                return endTime.plusDays(1).isAfter(ChronoLocalDate.from(cashflow.getTimestamp()));
            }
        };
    }
}
