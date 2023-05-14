package de.dhbw.ka.se.fibo.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Cashflow implements Comparable<Cashflow> {
    private CashflowType type;
    private BigDecimal overallValue;
    private LocalDateTime timestamp;
    private Category category;
    private Place place;
    private List<Item> items;

    public Cashflow(Category category, CashflowType type, BigDecimal overallValue, LocalDateTime timestamp, Place place) {
        setType(type);
        setOverallValue(overallValue);
        setTimestamp(timestamp);
        this.category = category;
        this.place = place;
    }

    public Cashflow(Category category, CashflowType type, BigDecimal overallValue, LocalDateTime timestamp, Place place, List<Item> items) {
        setType(type);
        setOverallValue(overallValue);
        setTimestamp(timestamp);
        this.category = category;
        this.place = place;
        this.items = items;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CashflowType getType() {
        return type;
    }

    public void setType(CashflowType type) {
        this.type = type;
    }

    public BigDecimal getOverallValue() {
        return overallValue;
    }

    public void setOverallValue(BigDecimal overallValue) {
        this.overallValue = overallValue.setScale(2, RoundingMode.HALF_UP);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int compareTo(Cashflow other) {
        // It is a Public API that it is sorted DESC
        int result = other.getTimestamp().compareTo(getTimestamp());

        if (0 == result) {
            result = other.getOverallValue().compareTo(getOverallValue());
        }

        if (0 == result) {
            result = other.getPlace().compareTo(getPlace());
        }

        if (0 == result) {
            result = other.getCategory().compareTo(getCategory());
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Cashflow cashflow = (Cashflow) o;
        return type == cashflow.type && overallValue.equals(cashflow.overallValue) && timestamp.equals(cashflow.timestamp) && category == cashflow.category && Objects.equals(place, cashflow.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, overallValue, timestamp, category, place);
    }

    @Override
    public String toString() {
        return "Cashflow{" +
                "type=" + type +
                ", overallValue=" + overallValue +
                ", timestamp=" + timestamp +
                ", category=" + category +
                ", place=" + place +
                ", items=" + items +
                '}';
    }
}
