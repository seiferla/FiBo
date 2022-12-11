package de.dhbw.ka.se.fibo.models;

import de.dhbw.ka.se.fibo.R;

public enum CashflowType {
    INCOME, EXPENSE;

    public int getColor() {
        if (CashflowType.INCOME == this) {
            return R.color.income;
        } else {
            return R.color.expense;
        }
    }

    public char getSign() {
        if (CashflowType.INCOME == this) {
            return '+';
        } else {
            return '-';
        }
    }
}
