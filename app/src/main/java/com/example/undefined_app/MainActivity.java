package com.example.undefined_app;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.undefined_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int connection_status = 0;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        final ImageView semafor1 = findViewById(R.id.Semafor1);
        final ImageView semafor2 = findViewById(R.id.Semafor2);
        final ImageView semafor3 = findViewById(R.id.Semafor3);

        if (connection_status == 0) {
            semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_red));
            semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
            semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
        }
        if (connection_status == 1) {
            semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
            semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_orange));
            semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
        }
        if (connection_status == 2) {
            semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
            semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
            semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_green));
        }
    }

}