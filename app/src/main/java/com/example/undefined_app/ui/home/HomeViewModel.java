package com.example.undefined_app.ui.home;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.undefined_app.R;

public class HomeViewModel extends ViewModel {

    public String serverIP;

    public int connection_status = 0;


    public HomeViewModel() {
        setServerIP(null);
        setConnection_status(0);

    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getConnection_status() {
        return connection_status;
    }

    public void setConnection_status(int connection_status) {
        this.connection_status = connection_status;
    }
}