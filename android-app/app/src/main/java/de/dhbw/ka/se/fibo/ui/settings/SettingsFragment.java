package de.dhbw.ka.se.fibo.ui.settings;

import static android.content.ContentValues.TAG;
import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIStringRequest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.LoginActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

  private FragmentSettingsBinding binding;
  private MaterialButton deleteUserButton;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSettingsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    deleteUserButton = binding.deleteUser;

    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initializeDeleteButton();
  }

  private void initializeDeleteButton() {
    deleteUserButton.setOnClickListener(e -> openDialog());
  }

  private void openDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
    builder.setCancelable(true);
    builder.setTitle("Title");
    builder.setMessage("Message");

    Response.Listener<String> onSuccess = response -> {
      Toast successToast = Toast.makeText(this.getContext(), "User deleted", Toast.LENGTH_LONG);
      successToast.setGravity(Gravity.TOP, 0, 0);
      successToast.show();
      Log.i(TAG, "Successfully" + response);
    };

    Response.ErrorListener onError = error -> {
      if (error.networkResponse != null) {
        switch (error.networkResponse.statusCode) {
          case 401:
            Log.e(TAG, "Unauthorized");
            break;
          case 500:
            Log.e(TAG, "Internal Server Error");
            break;
          default:
            break;
        }

        Toast errorToast = Toast.makeText(this.getContext(), R.string.missing_token_error, Toast.LENGTH_LONG);
        errorToast.show();
        Log.e(TAG, String.valueOf(error));
      } else {
        Toast errorToast = Toast.makeText(this.getContext(), R.string.deleting_user_currently_unavailable, Toast.LENGTH_LONG);
        errorToast.show();
        Log.e(TAG, String.valueOf(error));
      }
    };

    builder.setPositiveButton("Confirm",(dialog,which) -> {
      String url = "/users/delete/";
      StringRequest stringRequest = createAPIStringRequest(url, Request.Method.DELETE, null, onSuccess, onError);

      ApplicationState.getInstance(this.getContext()).clearAuthorization();
      Intent i = new Intent(this.getActivity(), LoginActivity.class);
      startActivity(i);
    });

    builder.setNegativeButton("Cancel",(dialog,which) -> {
    });

    AlertDialog dialog=builder.create();
    dialog.show();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}