package com.canadainformation.di.module;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.canadainformation.di.scopes.AppScoped;
import com.canadainformation.schedulers.BaseSchedulerProvider;
import com.canadainformation.schedulers.SchedulerProvider;
import com.canadainformation.utils.DefaultNetworkWatcher;
import com.canadainformation.utils.NetworkWatcher;

import dagger.Module;
import dagger.Provides;

/**
 * Created By Akash
 * on 18,Dec,2019 : 11:42 AM
 */
@Module
public class UtilsModule {

    @Provides
    @AppScoped
    ConnectivityManager provideConnectivityManager(Application context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @AppScoped
    NetworkWatcher onlineChecker(ConnectivityManager cm) {
        return new DefaultNetworkWatcher(cm);
    }

    @AppScoped
    @Provides
    BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
