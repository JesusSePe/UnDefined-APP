package com.example.undefined_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.undefined_app.databinding.ActivityMainBinding;
import com.example.undefined_app.ui.home.HomeViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import ServerConfig.Server;
import ServerConfig.ServerInterface;
import lipermi.handler.CallHandler;
import lipermi.net.Client;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // Saving homeViewModel
    HomeViewModel homeViewModel;

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
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


        // Get connect button
        Button connect = findViewById(R.id.connect_button);
        connect.setOnClickListener(v -> {
            // Store ip at homeViewModel serverIP variable
            try {
                homeViewModel.setServerIP(serverInput.getText().toString());
            } catch (Exception e) {
                homeViewModel.setServerIP("");
            }
            System.out.println("Server: " + homeViewModel.getServerIP());
            new Conn().execute();
        });
    }

    @SuppressLint("StaticFieldLeak")
    class Conn extends AsyncTask<Void, AppCompatActivity, MainActivity> implements Serializable {

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected MainActivity doInBackground(Void... params) {
            Client client = null;
            Looper.prepare();
            try {
                System.out.println("Preparing Handler...");
                CallHandler callHandler = new CallHandler();
                System.out.println("Creating client...");
                client = new Client(homeViewModel.getServerIP(), 7777, callHandler);
                System.out.println("Initializing server...");
                ServerInterface pingService = (ServerInterface) client.getGlobal(ServerInterface.class);
                System.out.println("Pinging...");
                String msg = pingService.ping();
                System.out.println("Ping done");
                System.out.println("Response: " + msg);
                // If server responds with ping, the message will be successful, otherwise, will show an error.
                if (msg.equals("ping"))
                {
                    msg = "Servidor Disponible";
                    homeViewModel.setConnection_status(1);

                    // Update status lights
                    runOnUiThread(() -> {
                        final ImageView semafor1 = findViewById(R.id.Semafor1);
                        final ImageView semafor2 = findViewById(R.id.Semafor2);
                        final ImageView semafor3 = findViewById(R.id.Semafor3);

                        semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                        semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_orange));
                        semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));

                    });

                    System.out.println(msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    // Check if there's an active game
                    boolean active = false;
                    while (!active) {
                        active = pingService.checkActive();
                        if (!active) {
                            Thread.sleep(3000);
                        }
                    }


                    // Send username to server
                    if (homeViewModel.getConnection_status() >= 1) {
                        String uname = CheckUsername(getApplicationContext());
                        if (uname != null) {
                            String regResult = pingService.register(uname);
                            if (regResult.equals("true")) {
                                homeViewModel.setConnection_status(2);
                                // Update status lights
                                runOnUiThread(() -> {
                                    final ImageView semafor1 = findViewById(R.id.Semafor1);
                                    final ImageView semafor2 = findViewById(R.id.Semafor2);
                                    final ImageView semafor3 = findViewById(R.id.Semafor3);

                                    semafor1.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                                    semafor2.setForeground(getResources().getDrawable(R.drawable.ic_cercle_gray));
                                    semafor3.setForeground(getResources().getDrawable(R.drawable.ic_cercle_green));

                                });
                                // Get new kahoot info
                                HashMap <String, String> info = null;
                                while (info == null) {
                                    info = pingService.newGameData();
                                    if (info == null) {
                                        Thread.sleep(2000);
                                    }
                                }

                                // Redirect to new Activity.
                                Thread.sleep(Integer.parseInt(Objects.requireNonNull(info.get("temps_inici")))* 1000L);
                                sendMessage(info, pingService, uname);

                            } else if (regResult.equals("KAD-0001")) {
                                runOnUiThread(() -> {
                                    // Show bad username error
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("ERROR nom d'usuari ja existent.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                });

                            // No connection
                            } else {
                                runOnUiThread(() -> {
                                    // Show generic error
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("ERROR desconegut.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                });
                            }
                        }
                }
                else
                {
                    System.out.println("Connexió fallida");
                    msg = "Connexió fallida";
                }

                }

                client.close();
            } catch (IOException | InterruptedException e) {
                System.out.println("Something went wrong");
                e.printStackTrace();
            }
            // Close connection
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Looper.loop();
            return null;
        }

        protected String CheckUsername (Context cont) {
            String uname;
            // Send username to server
            // Get username from SharedPreferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(cont);
            // Show username
            uname = sharedPref.getString("username", "UnDefined");
            if (uname.equals("UnDefined")) {
                Toast.makeText(cont, "ERROR de connexió.", Toast.LENGTH_SHORT).show();
                uname = null;
            }
            return uname;
        }

        protected void sendMessage(HashMap<String, String> gameData, ServerInterface server, String uname) {
            Intent intent = new Intent(getApplicationContext(), Game.class);
            intent.putExtra("pregunta", gameData.get("pregunta"));
            intent.putExtra("resposta1", gameData.get("resposta1"));
            intent.putExtra("resposta2", gameData.get("resposta2"));
            intent.putExtra("resposta3", gameData.get("resposta3"));
            intent.putExtra("resposta4", gameData.get("resposta4"));
            intent.putExtra("temps_preguntes", gameData.get("temps_preguntes"));
            intent.putExtra("server", (Serializable) server);
            intent.putExtra("uname", uname);
            startActivity(intent);
        }
    }
}