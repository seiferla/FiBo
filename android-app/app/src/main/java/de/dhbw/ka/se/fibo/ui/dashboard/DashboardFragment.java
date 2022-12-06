package de.dhbw.ka.se.fibo.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentDashboardBinding;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.Category;

public class DashboardFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentDashboardBinding binding;
    private MaterialDatePicker<Pair<Long, Long>> picker;
    private Date startDate;
    private Date endDate;
    private PieChart pieChart;

    private static final int PIE_CHART_ANIMATION_DURATION = 750;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeDateCard();
        createDatePicker();
        initializePieChart(root);
        return root;
    }

    private void initializePieChart(View root) {
        Context context = requireContext();
        List<Cashflow> cashflows = ApplicationState.getInstance(context).getCashflows();

        Map<Category, BigDecimal> expensesPerCategory = new HashMap<>();

        for (Cashflow cashflow : cashflows) {
            // TODO: Check whether cashflow is inside the timespan

            Category category = cashflow.getCategory();

            BigDecimal newValue = expensesPerCategory
                    .computeIfAbsent(category, x -> BigDecimal.ZERO)
                    .add(cashflow.getOverallValue());

            expensesPerCategory.put(category, newValue);
        }

        Log.v("FiBo", expensesPerCategory.toString());
        ArrayList<PieEntry> entries = new ArrayList<>();
        int[] colors = new int[expensesPerCategory.size()];

        int i = 0;
        for (Map.Entry<Category, BigDecimal> entrySet : expensesPerCategory.entrySet()) {
            Category category = entrySet.getKey();
            PieEntry entry = new PieEntry(entrySet.getValue().floatValue());
            entry.setLabel(context.getText(category.getName()).toString());

            entries.add(entry);
            colors[i++] = category.getColor();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors, context);
        dataSet.setValueFormatter(new CurrencyValueFormatter());
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);

        pieChart = root.findViewById(R.id.dashboard_piechart);
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(pieData);
        pieChart.getDescription().setText("");
        pieChart.setCenterTextSizePixels(12f);
        pieChart.getLegend().setTextSize(12f);

        pieChart.animateY(DashboardFragment.PIE_CHART_ANIMATION_DURATION, Easing.EaseInOutQuad);
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

        picker.addOnPositiveButtonClickListener(e -> {
            // TODO: update pie chart
            pieChart.animateY(DashboardFragment.PIE_CHART_ANIMATION_DURATION, Easing.EaseInOutQuad);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (null == e) {
            return;
        }

        Log.i("FiBo",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        // noop
    }
}