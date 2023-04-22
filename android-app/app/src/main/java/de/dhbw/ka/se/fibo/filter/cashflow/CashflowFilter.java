package de.dhbw.ka.se.fibo.filter.cashflow;

import java.util.List;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;

public interface CashflowFilter {
    Predicate<Cashflow> getPredicate();
}
