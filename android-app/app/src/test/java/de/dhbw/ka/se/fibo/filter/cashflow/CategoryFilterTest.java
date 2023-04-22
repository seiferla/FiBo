package de.dhbw.ka.se.fibo.filter.cashflow;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

public class CategoryFilterTest {

    /**
     * Test for filtering with no Category should accept everything
     */
    @Test
    public void filterWithoutCategoryTest() {
        Predicate<Cashflow> categoryFilter = new CategoryFilter(new HashSet<>()).getPredicate();

        Cashflow expense = new Cashflow(Category.CLOTHES,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 26),
                new Place("Adidas", "Zara Straße"));

        Cashflow income = new Cashflow(Category.RESTAURANT,
                CashflowType.INCOME,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 23),
                new Place("Oxford", "Adidas Straße"));

        assertTrue(categoryFilter.test(expense));
        assertTrue(categoryFilter.test(income));
    }

    /**
     * Test for filtering with one Category should not accept this Category
     */
    @Test
    public void filterWithOneCategoryTest() {
        Predicate<Cashflow> categoryFilter = new CategoryFilter(Set.of(Category.CLOTHES)).getPredicate();

        Cashflow expense = new Cashflow(Category.CLOTHES,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 26),
                new Place("Adidas", "Zara Straße"));

        Cashflow income = new Cashflow(Category.RESTAURANT,
                CashflowType.INCOME,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 23),
                new Place("Oxford", "Adidas Straße"));

        assertFalse(categoryFilter.test(expense));
        assertTrue(categoryFilter.test(income));
    }

    /**
     * Test for filtering with multiple Categories should not accept this Categories, but others should still be accepted
     */
    @Test
    public void filterWithMultipleCategoryTest() {
        Predicate<Cashflow> categoryFilter = new CategoryFilter(Set.of(Category.CLOTHES, Category.RESTAURANT)).getPredicate();

        Cashflow expense = new Cashflow(Category.CLOTHES,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 26),
                new Place("Adidas", "Zara Straße"));

        Cashflow income = new Cashflow(Category.RESTAURANT,
                CashflowType.INCOME,
                BigDecimal.valueOf(10),
                LocalDateTime.of(2023, 4, 22, 14, 23),
                new Place("Oxford", "Adidas Straße"));

        Cashflow otherExpense = new Cashflow(Category.EDUCATION,
                CashflowType.EXPENSE,
                BigDecimal.valueOf(12),
                LocalDateTime.of(2023, 4, 22, 14, 23),
                new Place("DHBW", "Erzberger Straße"));

        assertFalse(categoryFilter.test(expense));
        assertFalse(categoryFilter.test(income));
        assertTrue(categoryFilter.test(otherExpense));
    }
}