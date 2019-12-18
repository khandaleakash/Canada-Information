package com.canadainformation.view.activity;

import android.os.Bundle;

import com.canadainformation.R;
import com.canadainformation.utils.ActivityUtils;
import com.canadainformation.view.fragment.InformationFragment;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:33 PM
 */
public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    InformationFragment mInjectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up fragment
        InformationFragment fragment = (InformationFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = mInjectedFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.container);
        }
    }
}
