package com.canadainformation.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created By Akash
 * on 18,Dec,2019 : 11:45 AM
 */

/**
 * Custom Network Checker
 */

public class DefaultNetworkWatcher implements NetworkWatcher {

    private final ConnectivityManager connectivityManager;

    public DefaultNetworkWatcher(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public boolean isNetworkAvailable() {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
