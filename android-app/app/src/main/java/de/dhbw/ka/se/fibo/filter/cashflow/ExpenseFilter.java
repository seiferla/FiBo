package de.dhbw.ka.se.fibo.filter.cashflow;

import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;

public class ExpenseFilter implements CashflowFilter {


    @Override
    public Predicate<Cashflow> getPredicate() {
        return new Predicate<Cashflow>() {
            @Override
            public boolean test(Cashflow cashflow) {
                return cashflow.getType().equals(CashflowType.EXPENSE);
            }
        };
    }
}
