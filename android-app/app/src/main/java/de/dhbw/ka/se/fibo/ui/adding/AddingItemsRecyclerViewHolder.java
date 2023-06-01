package de.dhbw.ka.se.fibo.ui.adding;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import de.dhbw.ka.se.fibo.R;

public class AddingItemsRecyclerViewHolder extends RecyclerView.ViewHolder {

    final ConstraintLayout layoutWrapper;
    final MaterialTextView itemName;
    final MaterialTextView itemPrice;
    final MaterialTextView itemAmount;

    public AddingItemsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        layoutWrapper = itemView.findViewById(R.id.adding_items_row_layout_wrapper);
        itemName = itemView.findViewById(R.id.adding_items_row_item_name);
        itemPrice = itemView.findViewById(R.id.adding_items_row_price);
        itemAmount = itemView.findViewById(R.id.adding_items_row_item_amount);
    }
}
