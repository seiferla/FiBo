package de.dhbw.ka.se.fibo.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import de.dhbw.ka.se.fibo.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView materialCard;
    TextView imageView;
    MaterialTextView cardTitle, date, cashFlowValue;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        materialCard = itemView.findViewById(R.id.materialCardViewContainer);
        imageView = itemView.findViewById(R.id.initial);
        cardTitle = itemView.findViewById(R.id.cardTitle);
        date = itemView.findViewById(R.id.date);
        cashFlowValue = itemView.findViewById(R.id.cashFlowValue);
    }

    public void openDetails(){

    }
}
