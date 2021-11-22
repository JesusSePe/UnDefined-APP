package com.example.undefined_app.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public String serverIP;

    public int connection_status = 0;


    public HomeViewModel() {
        setServerIP(null);
        try {
            if (getConnection_status() >= 0) {
                setConnection_status(getConnection_status());
            } else {
                setConnection_status(0);
            }
        } catch (Exception e) {
            setConnection_status(0);
        }

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