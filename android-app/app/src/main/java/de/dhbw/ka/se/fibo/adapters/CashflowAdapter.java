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
import de.dhbw.ka.se.fibo.Helpers;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CashflowAdapter extends ArrayAdapter<Cashflow> {

  private final LayoutInflater inflater;

  public CashflowAdapter(@NonNull Context context, int resource, ArrayList<Cashflow> arrayList) {
    super(context, resource, arrayList);
    inflater = LayoutInflater.from(context);
  }

  @SuppressLint("SetTextI18n")
  // it's complaining about the `setText()` call where we input a sign (+ or -) and the formatted currency value
  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    Cashflow cashflow = getItem(position);
    CashflowType cashflowType = cashflow.getType();
    BigDecimal overallValue = cashflow.getOverallValue();

    @SuppressLint("ViewHolder") // TODO: Use a view holder to improve performance
    View view = inflater.inflate(R.layout.row_layout, null);

    TextView nameText = view.findViewById(R.id.cardTitle);
    nameText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    nameText.setText(cashflow.getName());

    TextView initial = view.findViewById(R.id.initial);
    initial.setText(String.valueOf(cashflow.getName().charAt(0)).toUpperCase());

    TextView overallValueText = view.findViewById(R.id.cashFlowValue);
    overallValueText.setText(
        cashflowType.getSign() + Helpers.formatBigDecimalCurrency(overallValue));
    overallValueText.setTextColor(
        getContext().getResources().getColor(cashflowType.getColor()));
    overallValueText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

    TextView timestamp = view.findViewById(R.id.date);
    Format formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    timestamp.setText(formatter.format(cashflow.getTimestamp()));

    return view;
  }

}