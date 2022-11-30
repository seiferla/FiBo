package de.dhbw.ka.se.fibo.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class Cashflow {
    private CashflowType type;
    private BigDecimal overallValue;
    private Date timestamp;
    private String name;

    public Cashflow(Category category, CashflowType type, BigDecimal overallValue, Date timestamp, String name) {
        this.setType(type);
        this.setOverallValue(overallValue);
        this.setTimestamp(timestamp);
        this.setName(name);
        this.category = category;
    }

    public Cashflow(CashflowType expense, BigDecimal valueOf, Date date, String bla) {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
