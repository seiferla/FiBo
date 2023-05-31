package de.dhbw.ka.se.fibo.ui.adding;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import de.dhbw.ka.se.fibo.R;

public class AddingItemsRecyclerViewHolder extends RecyclerView.ViewHolder {

    final MaterialCardView materialCard;
    final TextView detailsButton;
    final MaterialTextView itemName;
    final MaterialTextView itemDate;
    final MaterialTextView itemValue;

    public AddingItemsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        materialCard = itemView.findViewById(R.id.adding_items_row_material_card_view_container);
        detailsButton = itemView.findViewById(R.id.adding_items_row_imageViewDrawable);
        itemName = itemView.findViewById(R.id.adding_items_row_item_name);
        itemDate = itemView.findViewById(R.id.adding_items_row_date);
        itemValue = itemView.findViewById(R.id.adding_items_row_item_value);
    }
}
