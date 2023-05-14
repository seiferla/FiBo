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

public class EndTimeFilterTest {
    @Test
    public void endTimeFilterTest() {
        LocalDate endTime = LocalDate.of(2023, 4, 22);
        Predicate<Cashflow> endTimeFilter = new EndTimeFilter(endTime).getPredicate();

        Cashflow expenseBeforeEndTime = new Cashflow(new Category(1, "Kleidung", 2),
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 21, 14, 26),
                new Place(1, "Adidas", "Zara Straße"));

        Cashflow expenseAfterEndTime = new Cashflow(new Category(2, "Essen", 2),
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 5, 23, 14, 23),
                new Place(2, "Oxford", "Adidas Straße"));

        assertTrue(endTimeFilter.test(expenseBeforeEndTime));
        assertFalse(endTimeFilter.test(expenseAfterEndTime));
    }
}