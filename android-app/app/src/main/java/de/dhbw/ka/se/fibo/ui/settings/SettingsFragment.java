package de.dhbw.ka.se.fibo.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentAddingBinding;
import de.dhbw.ka.se.fibo.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

  private FragmentSettingsBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    SettingsViewModel settingsViewModel =
//        new ViewModelProvider(this).get(SettingsViewModel.class);
//
//    binding = FragmentSettingsBinding.inflate(inflater, container, false);
//    View root = binding.getRoot();
//
//    TextView textView = binding.textSettings;
//    settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//    return root
    binding = FragmentSettingsBinding.inflate(inflater, container, false);
    View view = binding.getRoot();

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}