package de.dhbw.ka.se.fibo.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class Cashflow implements Comparable<Cashflow> {
    private CashflowType type;
    private BigDecimal overallValue;
    private LocalDateTime timestamp;
    private Category category;
    private Place place;

    public Cashflow(Category category, CashflowType type, BigDecimal overallValue, LocalDateTime timestamp, Place place) {
        this.setType(type);
        this.setOverallValue(overallValue);
        this.setTimestamp(timestamp);
        this.category = category;
        this.place = place;
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

    @Override
    public int compareTo(Cashflow other) {
        // It is a Public API that it is sorted DESC
        return other.getTimestamp().compareTo(this.getTimestamp());
    }

    @Override
    public String toString() {
        return "Cashflow{" +
            "type=" + type +
            ", overallValue=" + overallValue +
            ", timestamp=" + timestamp +
            ", category=" + category +
            ", place=" + place +
            '}';
    }
}
