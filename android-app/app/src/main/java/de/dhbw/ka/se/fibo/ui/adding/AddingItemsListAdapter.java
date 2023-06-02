package de.dhbw.ka.se.fibo.ui.adding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.dhbw.ka.se.fibo.Helpers;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Item;

public class AddingItemsListAdapter extends RecyclerView.Adapter<AddingItemsRecyclerViewHolder> {
    private final List<Item> itemList;
    private final Context context;
    private AddingFragmentFragmentDialogEdit editDialog;


    public AddingItemsListAdapter(Context context, List<Item> itemList) {
        this.itemList = new ArrayList<>(itemList);
        this.context = context;
        editDialog = new AddingFragmentFragmentDialogEdit(context);
        editDialog.setAdapter(this);
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
        holder.itemAmount.setText(String.format("%s Stk.", currentItem.getAmount()));
        holder.itemPrice.setText(String.format("%s", Helpers.formatBigDecimalCurrency(currentItem.getValue())));
        holder.layoutWrapper.setOnClickListener(view -> openEditDialog(currentItem, position));
    }

    private void openEditDialog(Item item, int position) {
        editDialog.setCurrentItem(item);
        editDialog.setPosition(position);
        editDialog.show();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Item getItem(int position) {
        return itemList.get(position);
    }

    public void addItem(Item newItem) {
        itemList.add(newItem);
        notifyItemInserted(itemList.size());
    }

    public void deleteItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateItem(int position, Item item) {
        itemList.remove(position);
        itemList.add(position, item);
        notifyItemChanged(position);
    }
}
