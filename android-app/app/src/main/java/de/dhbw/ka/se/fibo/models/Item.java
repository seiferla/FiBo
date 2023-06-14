package de.dhbw.ka.se.fibo.models;

import java.math.BigDecimal;

public class Item {
    private float amount;
    private String name;
    private BigDecimal value;

    public Item(String name, BigDecimal value, float amount) {
        this.name = name;
        this.value = value;
        this.amount = amount;
    }

    public Item(String name, BigDecimal value) {
        this.amount = 1;
        this.name = name;
        this.value = value;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "amount=" + amount +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
