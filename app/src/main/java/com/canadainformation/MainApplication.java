package com.canadainformation;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:24 PM
 */
public class MainApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
