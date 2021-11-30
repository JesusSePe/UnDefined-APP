package com.example.undefined_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ServerConfig.Server;
import ServerConfig.ServerInterface;

public class Game extends AppCompatActivity {
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // Get gameData
        Intent i = getIntent();
        String pregunta = i.getStringExtra("pregunta");
        String resp1 = i.getStringExtra("resposta1");
        String resp2 = i.getStringExtra("resposta2");
        String resp3 = i.getStringExtra("resposta3");
        String resp4 = i.getStringExtra("resposta4");
        String timeout = i.getStringExtra("temps_preguntes");
        ServerInterface serverService = (ServerInterface) i.getSerializableExtra("server");
        String uname = i.getStringExtra("uname");

        Button opt1 = findViewById(R.id.button1);
        Button opt2 = findViewById(R.id.button2);
        Button opt3 = findViewById(R.id.button3);
        Button opt4 = findViewById(R.id.button4);

        EditText editTimeOut = findViewById(R.id.timeout);

        // Set buttons text
        opt1.setText(resp1);
        opt2.setText(resp2);
        opt3.setText(resp3);
        opt4.setText(resp4);

        // Set timeout
        editTimeOut.setText(timeout);

        // Register button click event
        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = opt1.getText().toString();
                serverService.answer(uname, resposta);
            }
        });

        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = opt1.getText().toString();
                serverService.answer(uname, resposta);
            }
        });

        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = opt1.getText().toString();
                serverService.answer(uname, resposta);
            }
        });

        opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resposta = opt1.getText().toString();
                serverService.answer(uname, resposta);
            }
        });

    }

    public void begin(View view) {
        timer.schedule(task, 1000, 1000);
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            runOnUiThread(() -> {
                TextView timeout = findViewById(R.id.timeout);
                Integer secondLeft = Integer.parseInt(timeout.getText().toString());
                timeout.setText(secondLeft-1);
                if (secondLeft < 0) {
                    timer.cancel();
                    timeout.setText ("TIME OUT");
                    // Disable buttons
                    findViewById(R.id.button1).setEnabled(false);
                    findViewById(R.id.button1).setBackgroundColor(Color.parseColor("#3A5A57"));
                    findViewById(R.id.button2).setEnabled(false);
                    findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#3A5A57"));
                    findViewById(R.id.button3).setEnabled(false);
                    findViewById(R.id.button3).setBackgroundColor(Color.parseColor("#3A5A57"));
                    findViewById(R.id.button4).setEnabled(false);
                    findViewById(R.id.button4).setBackgroundColor(Color.parseColor("#3A5A57"));
                }
            });
        }
    };
}
