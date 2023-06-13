package de.dhbw.ka.se.fibo.ui.adding;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.models.Item;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class AddingFragmentFragmentDialogAdd extends AddingFragmentDialog {

    public AddingFragmentFragmentDialogAdd(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnPositiveButtonListener();
        setOnNegativeButtonListener();
        addingFragmentDialogHeadingText.setText(R.string.add_article_dialog_heading);
    }

    public void setOnPositiveButtonListener() {
        saveButton.setOnClickListener(v -> {
            if (!ActivityUtils.checkValidInput(getFieldsToBeChecked())) {
                return;
            }
            if (getValue().isPresent() && getAmount().isPresent()) {
                getAdapter().addItem(new Item(getName(), getValue().get(), getAmount().get()));
                dismiss();
            }
        });
    }


    public void setOnNegativeButtonListener() {
        cancelButton.setOnClickListener(v -> dismiss());
    }

}
