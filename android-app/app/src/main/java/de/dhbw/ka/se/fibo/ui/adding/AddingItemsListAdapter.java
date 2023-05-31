package de.dhbw.ka.se.fibo.ui.adding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import de.dhbw.ka.se.fibo.Helpers;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Item;

public class AddingItemsListAdapter extends RecyclerView.Adapter<AddingItemsRecyclerViewHolder> {
    private final List<Item> itemArrayList;
    private final Context context;

    public AddingItemsListAdapter(Context context, SortedSet<Item> itemArrayList) {
        this.itemArrayList = new ArrayList<>(itemArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public AddingItemsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_row, parent, false);
        return new AddingItemsRecyclerViewHolder(view);
    }

    public void onBindViewHolder(@NonNull AddingItemsRecyclerViewHolder holder, int position) {
        Item currentItem = getItem(position);

        holder.itemName.setText(currentItem.getName());
        holder.itemAmount.setText(String.format("%s", currentItem.getAmount()));
        holder.itemPrice.setText(String.format("%s", Helpers.formatBigDecimalCurrency(currentItem.getValue())));
        holder.materialCard.setOnClickListener(view -> openEditDialog(currentItem, position));
    }

    //Todo implement method
    private void openEditDialog(Item item, int position) {
        Toast.makeText(context, position + ". " + item.getName() + " " + Helpers.formatBigDecimalCurrency(item.getValue()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Item getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
