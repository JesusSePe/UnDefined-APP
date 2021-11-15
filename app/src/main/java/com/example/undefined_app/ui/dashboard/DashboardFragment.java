package com.example.undefined_app.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.undefined_app.R;
import com.example.undefined_app.databinding.FragmentDashboardBinding;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // USER FRAGMENT
        // Get username input
        EditText usernameInput = this.requireActivity().findViewById(R.id.InputUsername);
        // Get sharedPreferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.requireActivity().getApplicationContext());
        // Show username
        String uname = sharedPref.getString("username", "UnDefined");
        usernameInput.setText(uname);

        // Get save button
        Button saveUsername = this.requireActivity().findViewById(R.id.saveButton);
        saveUsername.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.apply();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}