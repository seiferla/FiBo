package de.dhbw.ka.se.fibo.ui.adding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentAddingBinding;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;

public class AddingFragment extends Fragment {

    private FragmentAddingBinding binding;
    private MaterialDatePicker<Long> datePicker;
    private NavController navController;
    private TextInputEditText store;
    private TextInputEditText amount;
    private TextInputEditText dateText;
    private MaterialAutoCompleteTextView categoriesDropdown;
    private MaterialButton cancelButton;
    private MaterialButton okayButton;
    private TabLayout tabLayout;

    private CashflowType newCashFlowType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        store = binding.storeText;
        amount = binding.amountText;
        dateText = binding.dateText;
        categoriesDropdown = binding.categoryText;
        cancelButton = binding.cancel;
        okayButton = binding.okayButton;
        tabLayout = binding.tabLayout;
        setUpTabLayout();
        initializeButtons();
        initializeDropdownValues();
        createDatePicker();
        setUpDateTextField();
        return view;
    }

    private void setUpTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setDataWithSelectedTab(tab);
            }

            private void setDataWithSelectedTab(TabLayout.Tab tab) {
                if (tab == binding.tabLayout.getTabAt(0)) {
                    newCashFlowType = CashflowType.EXPENSE;
                    binding.amountText.setTextColor(requireActivity().getColor(CashflowType.EXPENSE.getColor()));
                } else if (tab == binding.tabLayout.getTabAt(1)) {
                    binding.amountText.setTextColor(requireActivity().getColor(CashflowType.INCOME.getColor()));
                    newCashFlowType = CashflowType.INCOME;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setDataWithSelectedTab(tab);
            }
        });
        tabLayout.selectTab(tabLayout.getTabAt(0));
    }

    private void initializeButtons() {
        cancelButton.setOnClickListener(e -> {
            navigateToHome();
        });

        okayButton.setOnClickListener(e -> {
            Cashflow newCashFlow = createCashFlow();
            if (null == newCashFlow) {
                //TODO handle invalid data
            } else {
                System.out.println(newCashFlow);
                //TODO add CashFlow to HomeFragmentList
                navigateToHome();
            }

        });
    }

    private Cashflow createCashFlow() {
        boolean isRequiredDataPresent = false;
        Category category;
        BigDecimal value;
        LocalDateTime date;
        Place place;
        try {
            isRequiredDataPresent = checkForRequiredData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isRequiredDataPresent) {
            List<Category> collect = Arrays.stream(Category.values()).filter(currentType -> {
                String name = requireActivity().getResources().getString(currentType.getName());
                return name.equals(getFieldValue(categoriesDropdown));
            }).collect(Collectors.toList());

            category = collect.get(0);

            value = BigDecimal.valueOf(Double.parseDouble(getFieldValue(amount)));

            //TODO use unique PlaceID
            //TODO let the user enter an Address
            place = new Place("dm", 220, "Adresse xy");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");

            date = LocalDate.parse(getFieldValue(dateText), formatter).atStartOfDay();

            return new Cashflow(category, newCashFlowType, value, date, place);
        }


        return null;
    }

    private String getFieldValue(TextView field) {
        return Objects.requireNonNull(field.getText()).toString();
    }

    private boolean checkForRequiredData() throws IllegalArgumentException {

        if (null == store.getText()
                | store.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Store must be set");
        } else if (null == amount.getText()
                | amount.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Amount must be set");
        } else if (null == dateText.getText()
                | dateText.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Date must be set");
        } else if (null == categoriesDropdown.getText()
                | categoriesDropdown.getText().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Category must be set");
        }
        //others are currently not stored in our database or not required

        return true;
    }

    private void navigateToHome() {
        navController.popBackStack();
    }

    private void setUpDateTextField() {
        dateText.setEnabled(false);
        dateText.setTextColor(requireContext().getColor(R.color.mainTextColor));
        binding.dateLayout.setEndIconOnClickListener(v -> datePicker.show(requireActivity().getSupportFragmentManager(), "datePick"));
    }

    private void initializeDropdownValues() {
        String[] items = getAllStringCategories();
        MaterialAutoCompleteTextView categoriesDropdown = (MaterialAutoCompleteTextView) binding.categoryText;
        categoriesDropdown.setSimpleItems(items);
        categoriesDropdown.setThreshold(4);
    }

    private String[] getAllStringCategories() {
        String[] categories = new String[Category.values().length];
        Category[] values = Category.values();
        for (int i = 0; i < values.length; i++) {
            Category value = values[i];
            categories[i] = requireContext().getResources().getString(value.getName());
        }

        return categories;
    }

    private void createDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.selectDate)
                .setNegativeButtonText(R.string.datePickerNegativeButtonText)
                .setPositiveButtonText(R.string.DatePickerPositiveButtonText)
                .setSelection(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Format formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
            dateText.setText(formatter.format(selection));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
