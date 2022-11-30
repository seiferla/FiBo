package de.dhbw.ka.se.fibo.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentHomeBinding;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Category;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<Cashflow> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;


    public HomeFragment() {
        super();
        initializeData();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.from(getContext()).inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ListAdapter(getContext(), arrayList));

        return view;
    }

    private void initializeData() {
        arrayList.add(new Cashflow(Category.RESTAURANT, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(Category.GESCHENK, CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "lidl"));
        arrayList.add(new Cashflow(Category.MOBILITÄT, CashflowType.EXPENSE, BigDecimal.valueOf(13.5), new Date(), "kaufland"));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}