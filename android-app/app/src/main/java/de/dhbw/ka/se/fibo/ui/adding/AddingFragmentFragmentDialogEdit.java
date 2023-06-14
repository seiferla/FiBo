package de.dhbw.ka.se.fibo.ui.adding;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Item;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class AddingFragmentFragmentDialogEdit extends AddingFragmentDialog {

    private Item currentItem;
    private int position;

    public AddingFragmentFragmentDialogEdit(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnDeleteButtonListener();
        setOnPositiveButtonListener();
        setOnNegativeButtonListener();
        deleteButton.setVisibility(View.VISIBLE);
        addingFragmentDialogHeadingText.setText(R.string.edit_article_dialog_heading);
    }


    @Override
    protected void onStart() {
        super.onStart();
        prefillData();
    }

    private void prefillData() {
        addingFragmentDialogItemAmount.setText(String.format("%s", currentItem.getAmount()));
        addingFragmentDialogItemPrice.setText(String.format("%s", currentItem.getValue()));
        addingFragmentDialogItemName.setText(currentItem.getName());
    }

    private void setOnDeleteButtonListener() {
        deleteButton.setOnClickListener(v -> {
            getAdapter().deleteItem(position);
            dismiss();
        });
    }

    public void setOnPositiveButtonListener() {
        saveButton.setOnClickListener(v -> {
            if (!ActivityUtils.checkValidInput(getFieldsToBeChecked())) {
                return;
            }
            if (getValue().isPresent() && getAmount().isPresent()) {
                getAdapter().updateItem(position, new Item(getName(), getValue().get(), getAmount().get()));
                dismiss();
            }
        });
    }


    public void setOnNegativeButtonListener() {
        cancelButton.setOnClickListener(v -> dismiss());
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

}
