package de.dhbw.ka.se.fibo.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Cashflow;

public class ListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<Cashflow> cashflowArrayList;

    public ListAdapter(ArrayList<Cashflow> cashflowArrayList) {
        this.cashflowArrayList = cashflowArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.cashFlowValue.setText(cashflowArrayList.get(position).getOverallValue().toString());
        holder.cardTitle.setText(cashflowArrayList.get(position).getName());
        holder.date.setText(cashflowArrayList.get(position).getTimestamp().toString());

    }

    @Override
    public int getItemCount() {
        return cashflowArrayList.size();
    }
}
