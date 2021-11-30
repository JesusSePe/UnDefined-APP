package com.example.undefined_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import ServerConfig.Server;
import ServerConfig.ServerInterface;

public class Game extends AppCompatActivity implements Serializable{
    Timer timer = new Timer();

    // Retrieve serverInterface
    //Intent i = getIntent();
    //String server = i.getStringExtra("server");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // Get gameData
        Intent i = getIntent();
        String pregunta = i.getStringExtra("pregunta");
        // No more questions, Kahoot has finished
        /*if (pregunta.equals(null)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        }*/
        // If there's a question, keep storing data.
        String resp1 = i.getStringExtra("resposta1");
        String resp2 = i.getStringExtra("resposta2");
        String resp3 = i.getStringExtra("resposta3");
        String resp4 = i.getStringExtra("resposta4");
        String timeout = i.getStringExtra("temps_preguntes");
        String uname = i.getStringExtra("uname");

        Button opt1 = findViewById(R.id.button1);
        Button opt2 = findViewById(R.id.button2);
        Button opt3 = findViewById(R.id.button3);
        Button opt4 = findViewById(R.id.button4);

        TextView editTimeOut = findViewById(R.id.timeout);

        // Set buttons text
        opt1.setText(resp1);
        opt2.setText(resp2);
        opt3.setText(resp3);
        opt4.setText(resp4);

        // Set timeout
        editTimeOut.setText(timeout);

        // Register button click event
        opt1.setOnClickListener(v -> {
            String resposta = opt1.getText().toString();
            //serverService.answer(uname, resposta);
            disableButtons(1);
        });

        opt2.setOnClickListener(v -> {
            String resposta = opt2.getText().toString();
            //serverService.answer(uname, resposta);
            disableButtons(2);
        });

        opt3.setOnClickListener(v -> {
            String resposta = opt3.getText().toString();
            //serverService.answer(uname, resposta);
            disableButtons(3);
        });

        opt4.setOnClickListener(v -> {
            String resposta = opt4.getText().toString();
            //serverService.answer(uname, resposta);
            disableButtons(4);
        });

    }

    public void begin() {
        timer.schedule(task, 1000);
    }

    TimerTask task = new TimerTask() {

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            runOnUiThread(() -> {
                TextView timeout = findViewById(R.id.timeout);
                int secondLeft = Integer.parseInt(timeout.toString());
                timeout.setText(secondLeft-1);
                begin();
                if (secondLeft < 0) {
                    timer.cancel();
                    timeout.setText ("TIME IS OUT");
                    disableButtons();
                }
            });
        }
    };

    public void disableButtons(){
        runOnUiThread(() -> {
            // Disable buttons
            findViewById(R.id.button1).setEnabled(false);
            findViewById(R.id.button1).setBackgroundColor(Color.parseColor("#3A5A57"));
            findViewById(R.id.button2).setEnabled(false);
            findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#3A5A57"));
            findViewById(R.id.button3).setEnabled(false);
            findViewById(R.id.button3).setBackgroundColor(Color.parseColor("#3A5A57"));
            findViewById(R.id.button4).setEnabled(false);
            findViewById(R.id.button4).setBackgroundColor(Color.parseColor("#3A5A57"));
            //nextQuestion();
        });
    }

    public void disableButtons(Integer btn){
        runOnUiThread(() -> {
            // Disable buttons
            findViewById(R.id.button1).setEnabled(false);
            findViewById(R.id.button2).setEnabled(false);
            findViewById(R.id.button3).setEnabled(false);
            findViewById(R.id.button4).setEnabled(false);
            if (btn != 1) {
                findViewById(R.id.button1).setBackgroundColor(Color.parseColor("#3A5A57"));
            }
            if (btn != 2) {
                findViewById(R.id.button2).setBackgroundColor(Color.parseColor("#3A5A57"));
            }
            if (btn != 3) {
                findViewById(R.id.button3).setBackgroundColor(Color.parseColor("#3A5A57"));
            }
            if (btn != 4) {
                findViewById(R.id.button4).setBackgroundColor(Color.parseColor("#3A5A57"));
            }
            //nextQuestion();
        });
    }

    /*public void nextQuestion(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Get new kahoot info
                HashMap<String, String> info = new HashMap<>();
                info.put("pregunta", i.getStringExtra("pregunta"));
                // If info.pregunta is not updated, that means the new question didn't start yet.
                while (info.get("pregunta").equals(i.getStringExtra("pregunta"))) {
                    info = serverService.newGameData();
                    if (info.get("pregunta").equals(i.getStringExtra("pregunta"))) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMessage(info, server, i.getStringExtra("uname"));
                    }
                }
            }
        });

    }*/

    protected void sendMessage(HashMap<String, String> gameData, String server, String uname){
        Intent intent = new Intent(getApplicationContext(), Game.class);
        intent.putExtra("pregunta", gameData.get("pregunta"));
        intent.putExtra("resposta1", gameData.get("resposta1"));
        intent.putExtra("resposta2", gameData.get("resposta2"));
        intent.putExtra("resposta3", gameData.get("resposta3"));
        intent.putExtra("resposta4", gameData.get("resposta4"));
        intent.putExtra("temps_preguntes", gameData.get("temps_preguntes"));
        intent.putExtra("server", server);
        intent.putExtra("uname", uname);
        startActivity(intent);
    }
}
