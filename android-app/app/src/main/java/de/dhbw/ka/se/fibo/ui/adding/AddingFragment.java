package de.dhbw.ka.se.fibo.ui.adding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import de.dhbw.ka.se.fibo.databinding.FragmentAddingBinding;

public class AddingFragment extends Fragment {

    private FragmentAddingBinding binding;
    private MaterialDatePicker<Long> datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Button datePickerButton = binding.datePickerButton;
        createDatePicker();
        binding.dateTextField.setEnabled(false);
        datePickerButton.setOnClickListener(v -> datePicker.show(requireActivity().getSupportFragmentManager(), "datePick"));
        return view;
    }

    private void createDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Format formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
            dateText.setText(formatter.format(selection));
        });
    }
}
