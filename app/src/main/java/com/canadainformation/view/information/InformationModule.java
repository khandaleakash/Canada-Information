package com.canadainformation.view.information;

import com.canadainformation.di.scopes.ActivityScoped;
import com.canadainformation.di.scopes.FragmentScoped;
import com.canadainformation.provider.BaseResourceProvider;
import com.canadainformation.provider.ResourceProvider;
import com.canadainformation.view.activity.MainActivity;
import com.canadainformation.view.fragment.InformationFragment;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created By Akash
 * on 17,Dec,2019 : 8:29 PM
 */

@Module(includes = {InformationModule.InformationAbstractModule.class})
public class InformationModule {

    @ActivityScoped
    @Provides
    BaseResourceProvider provideResourceProvider(MainActivity context) {
        return new ResourceProvider(context);
    }

    @Module
    public interface InformationAbstractModule {
        @FragmentScoped
        @ContributesAndroidInjector
        InformationFragment informationFragment();
    }
}
