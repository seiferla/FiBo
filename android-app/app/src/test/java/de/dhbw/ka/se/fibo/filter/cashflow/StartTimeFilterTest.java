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

        Cashflow expenseAfterStartTime = new Cashflow(new Category(2, "Essen", 2),
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 5, 23, 14, 23),
                new Place(1, "Oxford", "Adidas Straße"));

        Cashflow expenseBeforeStartTime = new Cashflow(new Category(1, "Kleidung", 2),
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 2, 21, 14, 26),
                new Place(2, "Adidas", "Zara Straße"));

        assertTrue(startTimeFilter.test(expenseAfterStartTime));
        assertFalse(startTimeFilter.test(expenseBeforeStartTime));
    }
}