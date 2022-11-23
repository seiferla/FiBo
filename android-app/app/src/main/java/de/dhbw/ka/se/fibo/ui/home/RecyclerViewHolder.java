package de.dhbw.ka.se.fibo.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.dhbw.ka.se.fibo.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView cardTitle, date, cashFlowValue;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.initial);
        cardTitle = itemView.findViewById(R.id.cardTitle);
        date = itemView.findViewById(R.id.date);
        cashFlowValue = itemView.findViewById(R.id.cashFlowValue);

    }

}
