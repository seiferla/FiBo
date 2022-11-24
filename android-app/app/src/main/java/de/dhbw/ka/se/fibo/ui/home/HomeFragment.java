package de.dhbw.ka.se.fibo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import de.dhbw.ka.se.fibo.adapters.CashflowAdapter;
import de.dhbw.ka.se.fibo.databinding.FragmentHomeBinding;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

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

        arrayList.add(new Cashflow(Category.RESTAURANT, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(Category.GESCHENK, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(Category.MOBILITÄT, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(Category.SONSTIGE, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(Category.GESUNDHEIT, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));


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