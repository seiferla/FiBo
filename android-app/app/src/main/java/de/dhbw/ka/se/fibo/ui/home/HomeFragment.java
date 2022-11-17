package de.dhbw.ka.se.fibo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import de.dhbw.ka.se.fibo.adapters.CashflowAdapter;
import de.dhbw.ka.se.fibo.databinding.FragmentHomeBinding;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.cashFlowList;
        ArrayList<Cashflow> arrayList = new ArrayList<>();

        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(CashflowType.INCOME, BigDecimal.valueOf(120.5), new Date(), "Gehalt"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Bla"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Foo"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Foo"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Foo"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Foo"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Fasel"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Fasel"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Fasel"));
        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(120.5), new Date(), "Fasel"));

        ArrayAdapter<Cashflow> listAdapter = new CashflowAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(listAdapter);
       //TODO make onClick work
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("position: " + position);
            Cashflow clickedItem = (Cashflow) listView.getItemAtPosition(position);
            Toast.makeText(getActivity(), clickedItem.getOverallValue().toString(), Toast.LENGTH_LONG).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}