package de.dhbw.ka.se.fibo.ui.home;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

import de.dhbw.ka.se.fibo.Helpers;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;

public class ListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Cashflow> cashflowArrayList;
    private Context context;

    public ListAdapter(Context context, SortedSet<Cashflow> cashflowArrayList) {
        setHasStableIds(true);
        this.cashflowArrayList = new ArrayList<>(cashflowArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        Cashflow cashflow = this.getItem(position);
        CashflowType cashflowType = cashflow.getType();
        BigDecimal overallValue = cashflow.getOverallValue();

        holder.cardTitle.setText(cashflow.getName());
        holder.cardTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        holder.imageView.setText(String.valueOf(context.getResources().getText(cashflow.getCategory().getName())).substring(0, 1));
        holder.cashFlowValue.setText(cashflowType.getSign() + Helpers.formatBigDecimalCurrency(overallValue));
        holder.cashFlowValue.setTextColor(context.getResources().getColor(cashflowType.getColor()));
        context.getResources().getText(cashflow.getCategory().getName()).charAt(0);
        holder.cashFlowValue.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .padNext(2, '0')
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral('.')
                .padNext(2, '0')
                .appendValue(ChronoField.MONTH_OF_YEAR)
                .appendLiteral('.')
                .appendValue(ChronoField.YEAR)
                .appendLiteral(' ')
                .padNext(2, '0')
                .appendValue(ChronoField.HOUR_OF_DAY)
                .appendLiteral(':')
                .padNext(2, '0')
                .appendValue(ChronoField.MINUTE_OF_HOUR)
                .appendLiteral(" Uhr")
                .toFormatter(Locale.getDefault());
        holder.date.setText(formatter.format(cashflow.getTimestamp()));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Cashflow getItem(int position) {
        return cashflowArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return cashflowArrayList.size();
    }
}
