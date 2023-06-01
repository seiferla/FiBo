package de.dhbw.ka.se.fibo.ui.adding;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

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
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class AddingFragmentDialog extends Dialog {

    private final Context context;

    private AddingFragmentDialogBinding binding;
    private AddingItemsListAdapter adapter;

    MaterialButton saveButton;
    MaterialButton cancelButton;
    MaterialButton deleteButton;
    TextInputEditText addingFragmentDialogItemName;
    TextInputEditText addingFragmentDialogItemPrice;
    TextInputEditText addingFragmentDialogItemAmount;
    TextView addingFragmentDialogHeadingText;
    private TextInputLayout addingFragmentDialogItemAmountLayout;
    private TextInputLayout addingFragmentDialogItemPriceLayout;
    private TextInputLayout addingFragmentDialogItemNameLayout;
    private Map<TextInputLayout, String> fieldsToBeChecked;

    @Override
    protected void onStop() {
        super.onStop();
        addingFragmentDialogItemName.setText("");
        addingFragmentDialogItemName.clearFocus();
        addingFragmentDialogItemAmount.setText("");
        addingFragmentDialogItemAmount.clearFocus();
        addingFragmentDialogItemPrice.setText("");
        addingFragmentDialogItemPrice.clearFocus();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AddingFragmentDialogBinding.inflate(getLayoutInflater());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);

        addingFragmentDialogHeadingText = binding.addingFragmentDialogHeadingText;
        addingFragmentDialogItemName = binding.addingFragmentDialogItemName;
        addingFragmentDialogItemNameLayout = binding.addingFragmentDialogItemNameLayout;
        addingFragmentDialogItemPrice = binding.addingFragmentDialogItemPrice;
        addingFragmentDialogItemPriceLayout = binding.addingFragmentDialogItemPriceLayout;
        addingFragmentDialogItemAmount = binding.addingFragmentDialogItemAmount;
        addingFragmentDialogItemAmountLayout = binding.addingFragmentDialogItemAmountLayout;
        deleteButton = binding.addingFragmentDialogHeadingDeleteButton;
        deleteButton.setVisibility(View.INVISIBLE);

        saveButton = binding.addingFragmentDialogSaveButton;
        cancelButton = binding.addingFragmentDialogCancelButton;

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

    String getName() {
        return ActivityUtils.getFieldValue(addingFragmentDialogItemName);
    }

    public Map<TextInputLayout, String> getFieldsToBeChecked() {
        return fieldsToBeChecked;
    }

    public AddingItemsListAdapter getAdapter() {
        return adapter;
    }

    Optional<BigDecimal> getValue() {
        try {
            return Optional.of(new BigDecimal(ActivityUtils.getFieldValue(addingFragmentDialogItemPrice)));
        } catch (NumberFormatException e) {
            addingFragmentDialogItemPriceLayout.setError(context.getString(R.string.invalid_input_error));
        }
        return Optional.empty();
    }

    Optional<Float> getAmount() {
        try {
            return Optional.of(Float.parseFloat(ActivityUtils.getFieldValue(addingFragmentDialogItemAmount)));
        } catch (NumberFormatException e) {
            addingFragmentDialogItemAmountLayout.setError(context.getString(R.string.invalid_input_error));
        }
        return Optional.empty();
    }


    public AddingFragmentDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

}
