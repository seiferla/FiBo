package de.dhbw.ka.se.fibo.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = binding.recyclerview;
        actionButton = binding.floatingButton;
        swipeRefreshLayout = binding.swipeContainer;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(requireContext(), R.drawable.card_divider);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(verticalDivider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ListAdapter(getContext(), ApplicationState.getInstance(requireContext()).getCashflows()));
        actionButton.setOnClickListener(e -> Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_adding));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            try {
                Toast.makeText(requireContext(), R.string.list_refresh_in_progress, Toast.LENGTH_SHORT).show();

                ApplicationState.getInstance(requireContext()).syncCashflows(result -> {
                    swipeRefreshLayout.setRefreshing(false);

                    if (!result.wasSuccessful()) {
                        Toast.makeText(requireContext(), R.string.list_refresh_unsuccessful, Toast.LENGTH_LONG).show();
                        return;
                    }

                    ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();

                    requireActivity().runOnUiThread(() -> {
                        if (null == adapter) {
                            return;
                        }

                        adapter.clear();
                        adapter.addAll(new ArrayList<>(result.getCashflows()));
                    });
                });
            } catch (IllegalStateException e) {
                swipeRefreshLayout.setRefreshing(false);

                Toast.makeText(requireContext(), R.string.list_refresh_during_illegal_state, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}