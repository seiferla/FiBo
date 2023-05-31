package de.dhbw.ka.se.fibo.ui.adding;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.AddingFragmentDialogBinding;
import de.dhbw.ka.se.fibo.models.Item;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class AddingDialog extends Dialog {

    private boolean isEditingMode;
    private final Context context;

    private AddingFragmentDialogBinding binding;
    private MaterialButton saveButton;
    private MaterialButton cancelButton;
    private TextInputEditText addingFragmentDialogItemName;
    private TextInputEditText addingFragmentDialogItemPrice;
    private TextInputEditText addingFragmentDialogItemAmount;
    private AddingItemsListAdapter adapter;
    private TextInputLayout addingFragmentDialogItemAmountLayout;
    private TextInputLayout addingFragmentDialogItemPriceLayout;
    private TextInputLayout addingFragmentDialogItemNameLayout;
    private Map<TextInputLayout, String> fieldsToBeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AddingFragmentDialogBinding.inflate(getLayoutInflater());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());

        addingFragmentDialogItemName = binding.addingFragmentDialogItemName;
        addingFragmentDialogItemNameLayout = binding.addingFragmentDialogItemNameLayout;
        addingFragmentDialogItemPrice = binding.addingFragmentDialogItemPrice;
        addingFragmentDialogItemPriceLayout = binding.addingFragmentDialogItemPriceLayout;
        addingFragmentDialogItemAmount = binding.addingFragmentDialogItemAmount;
        addingFragmentDialogItemAmountLayout = binding.addingFragmentDialogItemAmountLayout;

        saveButton = binding.addingFragmentDialogSaveButton;
        setOnPositiveButtonListener();
        cancelButton = binding.addingFragmentDialogCancelButton;
        setOnNegativeButtonListener();

        setRequiredFields();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setAdapter(AddingItemsListAdapter addingItemsListAdapter) {
        adapter = addingItemsListAdapter;
    }

    public void setRequiredFields() {
        fieldsToBeChecked = new HashMap<>();
        fieldsToBeChecked.put(addingFragmentDialogItemAmountLayout, context.getString(R.string.error_message_amount_field));
        fieldsToBeChecked.put(addingFragmentDialogItemNameLayout, context.getString(R.string.error_message_name_field));
        fieldsToBeChecked.put(addingFragmentDialogItemPriceLayout, context.getString(R.string.error_message_price_field));

    }

    private String getName() {
        return ActivityUtils.getFieldValue(addingFragmentDialogItemName);
    }

    private Optional<BigDecimal> getValue() {
        try {
            return Optional.of(new BigDecimal(ActivityUtils.getFieldValue(addingFragmentDialogItemPrice)));
        } catch (NumberFormatException e) {
            addingFragmentDialogItemPriceLayout.setError(context.getString(R.string.invalid_input_error));
        }
        return Optional.empty();
    }

    private Optional<Float> getAmount() {
        try {
            return Optional.of(Float.parseFloat(ActivityUtils.getFieldValue(addingFragmentDialogItemAmount)));
        } catch (NumberFormatException e) {
            addingFragmentDialogItemAmountLayout.setError(context.getString(R.string.invalid_input_error));
        }
        return Optional.empty();
    }

    public void setOnPositiveButtonListener() {
        saveButton.setOnClickListener(v -> {
            if (!ActivityUtils.checkValidInput(fieldsToBeChecked)) {
                return;
            }
            if (getValue().isPresent() && getAmount().isPresent()) {
                adapter.addItem(new Item(getName(), getValue().get(), getAmount().get()));
                dismiss();
            }
        });
    }

    public void setOnNegativeButtonListener() {
        cancelButton.setOnClickListener(v -> dismiss());
    }

    public AddingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

}
