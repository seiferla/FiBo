package de.dhbw.ka.se.fibo.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

  private FragmentSettingsBinding binding;
  private MaterialButton deleteAccountButton;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSettingsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    deleteAccountButton = binding.deleteAccount;

    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeDeleteButton();
  }

  private void initializeDeleteButton() {

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}