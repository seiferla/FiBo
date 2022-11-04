package de.dhbw.ka.se.fibo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.dhbw.ka.se.fibo.Helpers;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;

public class CashflowAdapter extends ArrayAdapter<Cashflow> {

    private LayoutInflater inflater;

    public CashflowAdapter(@NonNull Context context, int resource, ArrayList<Cashflow> arrayList) {
        super(context, resource, arrayList);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Cashflow cashflow = this.getItem(position);
        CashflowType cashflowType = cashflow.getType();
        BigDecimal overallValue = cashflow.getOverallValue();

        @SuppressLint("ViewHolder") // TODO: Use a view holder to improve performance
        View view = this.inflater.inflate(R.layout.row_layout, null);

        TextView nameText = (TextView) view.findViewById(R.id.placeName);
        nameText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        nameText.setText(cashflow.getName());

        TextView overallValueText = (TextView) view.findViewById(R.id.overallValue);
        overallValueText.setText(cashflowType.getSign() + Helpers.formatBigDecimalCurrency(overallValue));
        overallValueText.setTextColor(
                getContext().getResources().getColor(cashflowType.getColor()));
        overallValueText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        TextView timestamp = (TextView) view.findViewById(R.id.timestamp);
        Format formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        timestamp.setText(formatter.format(cashflow.getTimestamp()));

        return view;
    }

}