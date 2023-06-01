package de.dhbw.ka.se.fibo.ui.adding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentAddingBinding;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Item;
import de.dhbw.ka.se.fibo.models.Place;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

public class AddingFragment extends Fragment {

    private FragmentAddingBinding binding;
    private MaterialDatePicker<Long> datePicker;
    private NavController navController;
    private TextInputEditText store;
    private TextInputEditText amount;
    private TextInputEditText dateText;
    private MaterialAutoCompleteTextView categoriesDropdown;
    private TextInputLayout storeLayout;
    private TextInputLayout amountLayout;
    private TextInputLayout dateTextLayout;
    private TextInputLayout categoriesDropdownLayout;
    private TextInputLayout addressLayout;
    private MaterialButton cancelButton;
    private MaterialButton okayButton;
    private TabLayout tabLayout;
    private TextInputEditText address;
    private CashflowType newCashFlowType;

    private MaterialButton addItemButton;

    private RecyclerView addingItemsRecyclerView;
    private AddingItemsListAdapter addingItemsListAdapter;
    private AddingFragmentDialog addingFragmentDialog;


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
        address = binding.addressText;

        storeLayout = binding.storeTextLayout;
        amountLayout = binding.amountLayout;
        dateTextLayout = binding.dateLayout;
        addressLayout = binding.addressTextLayout;
        categoriesDropdownLayout = binding.categoryLayout;

        addItemButton = binding.addItemButton;
        addingItemsRecyclerView = binding.addingFragmentRecyclerview;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpTabLayout();
        initializeButtons();
        initializeDropdownValues();
        createDatePicker();
        setUpDateTextField();
        setUpItems(view);
        setUpDialog();
    }

    private void setUpDialog() {
        addingFragmentDialog = new AddingFragmentFragmentDialogAdd(requireContext());
        addingFragmentDialog.setAdapter(addingItemsListAdapter);

    }

    private void setUpItems(View view) {
        addingItemsListAdapter = new AddingItemsListAdapter(getContext(), List.of());
        addingItemsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        addingItemsRecyclerView.setAdapter(addingItemsListAdapter);
    }

    private void setUpTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setDataWithSelectedTab(tab);
            }

            // TODO: Test that the hint changes when switching tabs
            private void setDataWithSelectedTab(TabLayout.Tab tab) {
                if (tab == binding.tabLayout.getTabAt(0)) {
                    newCashFlowType = CashflowType.EXPENSE;
                    binding.amountText.setTextColor(requireActivity().getColor(CashflowType.EXPENSE.getColor()));
                    binding.storeTextLayout.setHint(requireContext().getString(R.string.place));
                } else if (tab == binding.tabLayout.getTabAt(1)) {
                    binding.amountText.setTextColor(requireActivity().getColor(CashflowType.INCOME.getColor()));
                    newCashFlowType = CashflowType.INCOME;
                    binding.storeTextLayout.setHint(requireContext().getString(R.string.source));
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
        cancelButton.setOnClickListener(e -> navigateToHome());

        okayButton.setOnClickListener(e -> {
            Cashflow newCashFlow = createCashFlow();
            if (null == newCashFlow) {
                Toast.makeText(requireContext(), "Some required inputs are empty or wrong formatted", Toast.LENGTH_SHORT).show();
            } else {
                ApplicationState.getInstance(requireContext()).addCashflow(newCashFlow);
                navigateToHome();
            }

        });

        addItemButton.setOnClickListener(e -> openItemAddingDialog());

    }

    private void openItemAddingDialog() {
        binding.getRoot().clearFocus(); //prevents the keyboard from opening again unnecessarily
        addingFragmentDialog.show();
    }

    private Cashflow createCashFlow() {
        Category category;
        BigDecimal value;
        LocalDateTime date;
        Place place;

        if (checkForRequiredData()) {
            List<Category> collect = Arrays.stream(Category.values()).filter(currentType -> {
                String name = requireActivity().getResources().getString(currentType.getName());
                return name.equals(getFieldValue(categoriesDropdown));
            }).collect(Collectors.toList());

            category = collect.get(0);

            value = BigDecimal.valueOf(Double.parseDouble(getFieldValue(amount)));

            place = new Place(getFieldValue(store), getFieldValue(address));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");

            date = LocalDate.parse(getFieldValue(dateText), formatter).atStartOfDay();

            return new Cashflow(category, newCashFlowType, value, date, place, getAddedItems());
        }

        return null;
    }

    private List<Item> getAddedItems() {
        return addingItemsListAdapter.getItemList();
    }

    private String getFieldValue(TextView field) {
        return Objects.requireNonNull(field.getText()).toString();
    }

    private boolean checkForRequiredData() {
        Map<TextInputLayout, String> fieldsToBeChecked = new HashMap<>();
        if (CashflowType.EXPENSE == newCashFlowType) {
            fieldsToBeChecked.put(storeLayout, getString(R.string.error_message_store_field));
        } else {
            fieldsToBeChecked.put(storeLayout, getString(R.string.error_message_source_field));
        }
        fieldsToBeChecked.put(amountLayout, getString(R.string.error_message_price_field));
        fieldsToBeChecked.put(dateTextLayout, getString(R.string.error_message_date_field));
        fieldsToBeChecked.put(categoriesDropdownLayout, getString(R.string.error_message_category_field));
        fieldsToBeChecked.put(addressLayout, getString(R.string.error_message_address_field));

        return ActivityUtils.checkValidInput(fieldsToBeChecked);
    }

    private void navigateToHome() {
        navController.popBackStack();
    }

    private void setUpDateTextField() {
        dateText.setFocusable(View.NOT_FOCUSABLE);
        dateText.setOnClickListener(showDatePicker());
        binding.dateLayout.setEndIconOnClickListener(showDatePicker());
    }

    private View.OnClickListener showDatePicker() {
        return (view) -> datePicker.show(requireActivity().getSupportFragmentManager(), "datePick");
    }

    private void initializeDropdownValues() {
        String[] items = getAllStringCategories();
        MaterialAutoCompleteTextView categoriesDropdown = binding.categoryText;
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
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointBackward.now();
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.selectDate)
                .setNegativeButtonText(R.string.datePickerNegativeButtonText)
                .setPositiveButtonText(R.string.DatePickerPositiveButtonText)
                .setCalendarConstraints(constraintBuilder.setValidator(dateValidator).build())
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