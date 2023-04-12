package de.dhbw.ka.se.fibo.models;

public class Item {
    private float amount;
    private String name;
    private float value;

    public Item(String name, float value, float amount) {
        this.name = name;
        this.value = value;
        this.amount = amount;
    }

    public Item(String name, float value) {
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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
