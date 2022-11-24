package de.dhbw.ka.se.fibo.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import de.dhbw.ka.se.fibo.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView imageView;
    MaterialTextView cardTitle, date, cashFlowValue;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.initial);
        cardTitle = itemView.findViewById(R.id.cardTitle);
        date = itemView.findViewById(R.id.date);
        cashFlowValue = itemView.findViewById(R.id.cashFlowValue);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
