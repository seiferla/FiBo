package de.dhbw.ka.se.fibo.ui.dashboard;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Locale;

public class CurrencyValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return String.format(Locale.getDefault(), "%.2f", value) + " €";
    }

}
