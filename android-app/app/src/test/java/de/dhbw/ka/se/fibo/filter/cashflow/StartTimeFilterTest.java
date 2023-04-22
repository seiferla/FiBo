package de.dhbw.ka.se.fibo.filter.cashflow;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

public class StartTimeFilterTest {
    @Test
    public void startTimeFilterTest() {
        LocalDate startTime = LocalDate.of(2023, 3, 22);
        Predicate<Cashflow> startTimeFilter = new StartTimeFilter(startTime).getPredicate();

        Cashflow expenseAfterStartTime = new Cashflow(Category.RESTAURANT,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 5, 23, 14, 23),
                new Place("Oxford", "Adidas Straße"));

        Cashflow expenseBeforeStartTime = new Cashflow(Category.CLOTHES,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 2, 21, 14, 26),
                new Place("Adidas", "Zara Straße"));

        assertTrue(startTimeFilter.test(expenseAfterStartTime));
        assertFalse(startTimeFilter.test(expenseBeforeStartTime));
    }
}