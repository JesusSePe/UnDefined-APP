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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.undefined_app.databinding.ActivityMainBinding;

import java.io.IOException;

import Connection.Connect;
import lipermi.handler.CallHandler;
import lipermi.net.Client;

public class MainActivity extends AppCompatActivity {

    private String serverIP = null;

    private ActivityMainBinding binding;

    int connection_status = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get serverIP input
        EditText serverInput = findViewById(R.id.Kahoot_id_input);
        serverIP = serverInput.getText().toString();

        Button connect = findViewById(R.id.connect_button);
        connect.setOnClickListener(v -> new Conn().execute());

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
    @SuppressLint("StaticFieldLeak")
    class Conn extends AsyncTask<Void, Void, MainActivity> {

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected MainActivity doInBackground(Void... params) {
            Looper.prepare();
            try {
                CallHandler callHandler = new CallHandler();
                Client client = new Client(serverIP, 7777, callHandler);
                Connect testService = (Connect) client.getGlobal(Connect.class);
                String msg = testService.ping();
                // If server responds with ping, the message will be successful, otherwise, will show an error.
                if (msg.equals("ping"))
                {
                    msg = "Servidor Disponible";
                    final ImageView semafor1 = findViewById(R.id.Semafor1);
                    final ImageView semafor2 = findViewById(R.id.Semafor2);
                    final ImageView semafor3 = findViewById(R.id.Semafor3);

                    connection_status = 1;

                    semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                    semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_orange));
                    semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));

                }
                else
                {
                    msg = "Connexió fallida";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Looper.loop();
            return null;
        }

    }
}