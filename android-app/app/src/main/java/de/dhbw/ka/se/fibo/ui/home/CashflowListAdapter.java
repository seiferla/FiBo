package de.dhbw.ka.se.fibo.ui.home;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class CashflowListAdapter extends RecyclerView.Adapter<CashflowViewHolder> {

    private final List<Cashflow> cashflowArrayList;
    private final Context context;

    public CashflowListAdapter(Context context, SortedSet<Cashflow> cashflowArrayList) {
        setHasStableIds(true);
        this.cashflowArrayList = new ArrayList<>(cashflowArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public CashflowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cashflow_row, parent, false);
        return new CashflowViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CashflowViewHolder holder, int position) {
        Cashflow cashflow = getItem(position);
        CashflowType cashflowType = cashflow.getType();
        BigDecimal overallValue = cashflow.getOverallValue();
        holder.cardTitle.setText(cashflow.getPlace().getName());
        holder.cardTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        holder.imageView.setText(String.valueOf(context.getResources().getText(cashflow.getCategory().getName())).substring(0, 1));
        holder.cashFlowValue.setText(String.format("%s%s", cashflowType.getSign(), Helpers.formatBigDecimalCurrency(overallValue)));
        holder.cashFlowValue.setTextColor(context.getResources().getColor(cashflowType.getColor(), context.getTheme()));
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
                .toFormatter(Locale.getDefault());
        holder.date.setText(formatter.format(cashflow.getTimestamp()));
        holder.materialCard.setOnClickListener(view -> openDetailsPage(cashflow, position));

    }

    //Todo implement method
    private void openDetailsPage(Cashflow cashflow, int position) {
        Toast.makeText(context, position + ". " + cashflow.getPlace().getName() + " " + cashflow.getType().getSign() + Helpers.formatBigDecimalCurrency(cashflow.getOverallValue()), Toast.LENGTH_SHORT).show();
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
