package com.example.undefined_app.ui.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.undefined_app.MainActivity;
import com.example.undefined_app.R;
import com.example.undefined_app.databinding.FragmentHomeBinding;

import java.io.IOException;

import Connection.Connect;
import lipermi.handler.CallHandler;
import lipermi.net.Client;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ImageView semafor1 = getActivity().findViewById(R.id.Semafor1);
        final ImageView semafor2 = getActivity().findViewById(R.id.Semafor2);
        final ImageView semafor3 = getActivity().findViewById(R.id.Semafor3);

        System.out.println("Connection_status: " + homeViewModel.getConnection_status());

        switch (homeViewModel.getConnection_status()) {

            case 0:
                semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_red));
                semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                break;
            case 1:
                semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_orange));
                semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                break;
            case 2:
                semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_green));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}