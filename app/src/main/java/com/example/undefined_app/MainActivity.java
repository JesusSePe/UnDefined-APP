package com.example.undefined_app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.undefined_app.databinding.ActivityMainBinding;
import com.example.undefined_app.ui.home.HomeFragment;
import com.example.undefined_app.ui.home.HomeViewModel;

import java.io.IOException;

import Connection.Connect;
import lipermi.handler.CallHandler;
import lipermi.net.Client;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // Saving homeViewModel
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Get serverIP input
        EditText serverInput = findViewById(R.id.Kahoot_id_input);
        // Store ip at homeViewModel serverIP variable
        try {
            homeViewModel.setServerIP(serverInput.getText().toString());
        } catch (Exception e) {
            homeViewModel.setServerIP("");
        }

        // Get connect button
        Button connect = findViewById(R.id.connect_button);
        connect.setOnClickListener(v -> new Conn().execute());
    }

    @SuppressLint("StaticFieldLeak")
    class Conn extends AsyncTask<Void, Void, MainActivity> {

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected MainActivity doInBackground(Void... params) {
            Looper.prepare();
            try {
                CallHandler callHandler = new CallHandler();
                Client client = new Client(homeViewModel.getServerIP(), 7777, callHandler);
                Connect testService = (Connect) client.getGlobal(Connect.class);
                String msg = testService.ping();
                // If server responds with ping, the message will be successful, otherwise, will show an error.
                if (msg.equals("ping"))
                {
                    msg = "Servidor Disponible";
                    final ImageView semafor1 = findViewById(R.id.Semafor1);
                    final ImageView semafor2 = findViewById(R.id.Semafor2);
                    final ImageView semafor3 = findViewById(R.id.Semafor3);

                    homeViewModel.setConnection_status(1);

                    semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                    semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_orange));
                    semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));

                }
                else
                {
                    msg = "Connexió fallida";
                }

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Looper.loop();
            return null;
        }
    }
}