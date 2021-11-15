package com.example.undefined_app.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    public String username;

    public DashboardViewModel() {

        username = (null);
    }
}