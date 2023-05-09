package de.dhbw.ka.se.fibo.utils.backend;

import java.util.SortedSet;

import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

public class BackendSynchronizationResult {
    private SortedSet<Cashflow> cashflows;
    private SortedSet<Category> categories;
    private SortedSet<Place> places;
    private Throwable throwable = null;

    public BackendSynchronizationResult(SortedSet<Cashflow> cashflows, SortedSet<Category> categories, SortedSet<Place> places, Throwable throwable) {
        this.cashflows = cashflows;
        this.categories = categories;
        this.places = places;
        this.throwable = throwable;
    }

    public boolean wasSuccessful() {
        return null == throwable;
    }

    /**
     * @return null if it was successful
     */
    public Throwable getThrowable() {
        return throwable;
    }

    public SortedSet<Cashflow> getCashflows() {
        return cashflows;
    }

    public SortedSet<Category> getCategories() {
        return categories;
    }

    public SortedSet<Place> getPlaces() {
        return places;
    }
}
