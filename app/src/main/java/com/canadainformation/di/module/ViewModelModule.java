package com.canadainformation.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.canadainformation.di.factory.InformationViewModelFactory;
import com.canadainformation.di.scopes.AppScoped;
import com.canadainformation.view.viewmodel.InformationViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created By Akash
 * on 18,Dec,2019 : 11:38 AM
 */
@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(InformationViewModel.class)
    abstract ViewModel bindInformationViewModel(InformationViewModel informationViewModel);

    @Binds
    @AppScoped
    abstract ViewModelProvider.Factory bindViewModelFactory(InformationViewModelFactory factory);
}
