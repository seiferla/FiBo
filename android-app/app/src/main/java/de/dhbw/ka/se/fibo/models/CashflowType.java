package de.dhbw.ka.se.fibo.models;

import de.dhbw.ka.se.fibo.R;

public enum CashflowType {
    INCOME, EXPENSE;

    public int getColor() {
        if (this == INCOME) {
            return R.color.income;
        } else {
            return R.color.expense;
        }
    }

    public char getSign() {
        if (this == INCOME) {
            return '+';
        } else {
            return '-';
        }
    }
}
