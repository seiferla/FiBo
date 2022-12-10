package de.dhbw.ka.se.fibo.ui.dashboard;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentDashboardBinding;

import java.util.Date;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private MaterialDatePicker<Pair<Long, Long>> picker;
    private Date startDate;
    private Date endDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeDateCard();
        createDatePicker();
        return root;
    }

    private void initializeDateCard() {
        setDateCardTitle();
        setDateCardTime();
        setListener();
    }

    private void setListener() {
        binding.button.setOnClickListener(
                e -> picker.show(requireActivity().getSupportFragmentManager(), "date_pange_picker"));
    }

    private void setDateCardTime() {
        if (null == startDate) {
            startDate = new Date();
            startDate.setMonth(startDate.getMonth() - 1);
        }
        if (null == endDate) {
            endDate = new Date();
        }
        binding.dateStartEndText.setText(String.format("%s - %s", DateFormat.format("dd.MM", startDate),
                DateFormat.format("dd.MM", endDate)));
    }

    private void setDateCardTitle() {
        binding.datePickerTitle.setText(R.string.dateCardTitle);
    }

    private void createDatePicker() {
        picker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText(R.string.datePickerTitle)
                .build();

        //TODO implement method for "save" button
        picker.addOnPositiveButtonClickListener(e -> {
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}