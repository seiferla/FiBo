package de.dhbw.ka.se.fibo.filter.cashflow;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

public class ExpenseFilterTest {

    @Test
    public void acceptExpensesTest() {
        Predicate<Cashflow> expenseFilter = new ExpenseFilter().getPredicate();

        Cashflow expense = new Cashflow(Category.CLOTHES,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 26),
                new Place("Adidas", "Zara Straße"));

        assertTrue(expenseFilter.test(expense));
    }

    @Test
    public void rejectIncomeTest() {
        Predicate<Cashflow> expenseFilter = new ExpenseFilter().getPredicate();

        Cashflow expense = new Cashflow(Category.CLOTHES,
                CashflowType.INCOME,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 26),
                new Place("Adidas", "Zara Straße"));

        assertFalse(expenseFilter.test(expense));
    }
}