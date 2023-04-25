package de.dhbw.ka.se.fibo.filter.cashflow;

import java.util.Set;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.Category;

public class CategoryFilter implements CashflowFilter {
    Set<Category> hiddenCategories;

    public CategoryFilter(Set<Category> hiddenCategories) {
        this.hiddenCategories = hiddenCategories;
    }

    @Override
    public Predicate<Cashflow> getPredicate() {
        return cashflow -> !hiddenCategories.contains(cashflow.getCategory());
    }
}
