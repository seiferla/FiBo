package de.dhbw.ka.se.fibo.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentDashboardBinding;
import de.dhbw.ka.se.fibo.filter.cashflow.CategoryFilter;
import de.dhbw.ka.se.fibo.filter.cashflow.EndTimeFilter;
import de.dhbw.ka.se.fibo.filter.cashflow.ExpenseFilter;
import de.dhbw.ka.se.fibo.filter.cashflow.StartTimeFilter;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;

public class DashboardFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentDashboardBinding binding;
    private MaterialDatePicker<Pair<Long, Long>> picker;
    private Instant startInstant;

    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Category> hiddenCategories = new HashSet<>();
    private List<Category> orderedCategoryList;

    private static final int PIE_CHART_ANIMATION_DURATION = 750;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // gather current instant to be able to calculate the time we spent in this (user-interaction driven) method
        startInstant = Instant.now();

        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateCategories();

        initializeDateCard();
        createDatePicker();
        initializePieChart();

        initializeFilter();

        Duration duration = Duration.between(startInstant, Instant.now());
        long millis = duration.toMillis();
        if (1000 < millis) {
            Log.w("FiBo", "Creating dashboard view took " + millis + " ms (more than 1 s!)");
        } else {
            Log.v("FiBo", "Creating dashboard view took " + millis + " ms");
        }
    }

    private void populateCategories() {
        Context context = requireContext();

        orderedCategoryList = ApplicationState.getInstance(context)
                .getCashflows()
                .stream()
                .filter(x -> CashflowType.EXPENSE == x.getType())
                .map(Cashflow::getCategory)
                .collect(Collectors.toSet()) // make sure we have only one occurrence of each category
                .stream() // then sort by localized name of the category
                .sorted(Comparator.comparing(Category::getName))
                .collect(Collectors.toList());
    }

    private void initializeFilter() {
        Button filterButton = binding.openFilterOptions;
        filterButton.setOnClickListener(v -> {
            Context context = requireContext();

            boolean[] checkedItems = new boolean[orderedCategoryList.size()];
            CharSequence[] names = new CharSequence[orderedCategoryList.size()];

            for (int i = 0; i < checkedItems.length; i++) {
                Category category = orderedCategoryList.get(i);

                checkedItems[i] = !hiddenCategories.contains(category);
                names[i] = category.getName();
            }

            new MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.dashboard_filter_dialog_title))
                    .setMultiChoiceItems(
                            names,
                            checkedItems,
                            (dialog, which, isChecked) -> {
                                // noop
                            })
                    .setPositiveButton(R.string.apply, (dialog, which) -> {
                        for (int i = 0; i < checkedItems.length; i++) {
                            Category category = orderedCategoryList.get(i);
                            if (!checkedItems[i]) {
                                hiddenCategories.add(category);
                            } else {
                                hiddenCategories.remove(category);
                            }
                        }

                        // react to changes
                        initializePieChart();
                    })
                    .setCancelable(true)
                    .show();
        });
    }

    private void initializePieChart() {
        Context context = requireContext();

        Map<Category, BigDecimal> expensesPerCategory = getFilteredExpensesPerCategory();

        // TODO: Check whether we have no data in the selected time-span and show warning if appropiate

        // visual adaption in case categories were filtered out to improve UX
        if (hiddenCategories.isEmpty()) {
            binding.filteredCategoriesIndications.setText("");
        } else {
            binding.filteredCategoriesIndications.setText(context.getString(R.string.dashboard_filter_indication_categories, hiddenCategories.size()));
        }

        int i = 0;

        ArrayList<PieEntry> entries = new ArrayList<>();
        int[] colors = new int[expensesPerCategory.size()];

        for (Map.Entry<Category, BigDecimal> entrySet : expensesPerCategory.entrySet()) {
            Category category = entrySet.getKey();
            PieEntry entry = new PieEntry(entrySet.getValue().floatValue());
            entry.setLabel(category.getName());

            entries.add(entry);
            colors[i++] = category.getColor();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors, context);
        dataSet.setValueFormatter(new CurrencyValueFormatter());
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);

        PieChart pieChart = binding.dashboardPieChart;
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(pieData);
        pieChart.getDescription().setText("");
        pieChart.setCenterTextSize(12f);
        pieChart.getLegend().setTextSize(12f);
        pieChart.getLegend().setWordWrapEnabled(true);

        pieChart.animateY(DashboardFragment.PIE_CHART_ANIMATION_DURATION, Easing.EaseInOutQuad);
    }

    /**
     * Loads all cashflows from the application state, extracts all expenses and applies all active filters
     *
     * @return the expenses per category to be displayed to the user
     */
    @NonNull
    private Map<Category, BigDecimal> getFilteredExpensesPerCategory() {
        Context context = requireContext();

        SortedSet<Cashflow> cashflows = ApplicationState.getInstance(context).getCashflows();

        Stream<Cashflow> expensesStream = cashflows.stream().filter(new ExpenseFilter().getPredicate());

        // filter by start date
        if (null != startDate) {
            expensesStream = expensesStream.filter(new StartTimeFilter(startDate).getPredicate());
        }

        // filter by end date
        if (null != endDate) {
            expensesStream = expensesStream.filter(new EndTimeFilter(endDate).getPredicate());
        }

        // filter by hidden categories
        if (!hiddenCategories.isEmpty()) {
            expensesStream = expensesStream.filter(new CategoryFilter(hiddenCategories).getPredicate());
        }

        Map<Category, BigDecimal> expensesPerCategory = new HashMap<>();

        expensesStream.forEach(expense -> {
            Category category = expense.getCategory();

            // accumulate per category
            expensesPerCategory.put(category, expensesPerCategory
                    .computeIfAbsent(category, x -> BigDecimal.ZERO)
                    .add(expense.getOverallValue()));
        });

        return expensesPerCategory;
    }

    private void initializeDateCard() {
        setDateCardTitle();
        setDateCardTime();
        setDateCardButtonListener();
    }

    private void setDateCardButtonListener() {
        binding.button.setOnClickListener(
                e -> picker.show(requireActivity().getSupportFragmentManager(), "date_range_picker"));
    }

    private void setDateCardTime() {
        Context context = requireContext();

        if (null == startDate && null == endDate) {
            binding.dateStartEndText.setText(context.getText(R.string.timespanAllData));
        } else if (null != startDate && null != endDate) {
            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                    .padNext(2, '0')
                    .appendValue(ChronoField.DAY_OF_MONTH)
                    .appendLiteral('.')
                    .padNext(2, '0')
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral('.');

            if (startDate.getYear() != endDate.getYear()) {
                builder = builder.appendValue(ChronoField.YEAR);
            }

            DateTimeFormatter formatter = builder.toFormatter(Locale.getDefault());

            binding.dateStartEndText.setText(context.getString(R.string.timespanStartToEnd, formatter.format(startDate), formatter.format(endDate)));
        } else {
            Log.w("FiBo", "setDateCardTime(): startDate != endDate");
        }
    }

    private void setDateCardTitle() {
        binding.datePickerTitle.setText(R.string.dateCardTitle);
    }

    private void createDatePicker() {
        SortedSet<Cashflow> cashflows = ApplicationState.getInstance(requireContext())
                .getCashflows();
        CalendarConstraints.Builder builder = new CalendarConstraints.Builder();

        if (!cashflows.isEmpty()) {
            Cashflow newestCashflow = cashflows.first();
            Cashflow oldestCashflow = cashflows.last();

            builder.setStart(
                    oldestCashflow.getTimestamp().atZone(ZoneId.systemDefault()).toInstant()
                            .toEpochMilli());
            builder.setEnd(newestCashflow.getTimestamp().atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli());
        }

        picker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText(R.string.datePickerTitle)
                .setCalendarConstraints(builder.build())
                .build();

        picker.addOnPositiveButtonClickListener(e -> {
            startDate = Instant.ofEpochMilli(e.first).atZone(ZoneId.systemDefault())
                    .toLocalDate();
            endDate = Instant.ofEpochMilli(e.second).atZone(ZoneId.systemDefault()).toLocalDate();

            setDateCardTime();

            // update pie chart to only include data in the selected time-span
            initializePieChart();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // noop
    }

    @Override
    public void onNothingSelected() {
        // noop
    }
}