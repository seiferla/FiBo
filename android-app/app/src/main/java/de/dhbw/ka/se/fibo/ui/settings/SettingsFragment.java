package de.dhbw.ka.se.fibo.ui.settings;

import static android.content.ContentValues.TAG;
import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIStringRequest;

import android.content.Context;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.LoginActivity;
import de.dhbw.ka.se.fibo.R;
import de.dhbw.ka.se.fibo.SharedVolleyRequestQueue;
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
        deleteUserButton.setOnClickListener(e -> {
            openDialog();
        });
    }

    private void openDialog() {
        Context context = requireContext();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setCancelable(true);
        builder.setTitle("Title");
        builder.setMessage("Message");

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            deleteUserRequest();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });

        builder.show();
    }

    private void deleteUserRequest() {
        Response.Listener<String> onSuccess = response -> {
            Toast successToast = Toast.makeText(getContext(), R.string.user_successfully_deleted, Toast.LENGTH_LONG);
            successToast.setGravity(Gravity.TOP, 0, 0);
            successToast.show();
            ApplicationState.getInstance(getContext()).clearAuthorization();
            Log.i(TAG, "Successfully" + response);

            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
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

                Toast errorToast = Toast.makeText(getContext(), R.string.missing_token_error, Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, String.valueOf(error));
            } else {
                Toast errorToast = Toast.makeText(getContext(), R.string.deleting_user_currently_unavailable, Toast.LENGTH_LONG);
                errorToast.show();
                Log.e(TAG, String.valueOf(error));
            }
        };

        String url = "/users/delete/";
        StringRequest stringRequest = createAPIStringRequest(url,
                Request.Method.DELETE,
                null,
                onSuccess,
                onError,
                ApplicationState.getInstance(getContext()).getAccessToken());

        SharedVolleyRequestQueue requestQueue = SharedVolleyRequestQueue.getInstance(getContext());
        requestQueue.addToRequestQueue(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}