package com.canadainformation.data;

import com.canadainformation.data.local.InformationDao;
import com.canadainformation.data.local.InformationLocalDataModule;
import com.canadainformation.data.local.InformationLocalDataSource;
import com.canadainformation.data.remote.InformationDataService;
import com.canadainformation.data.remote.InformationRemoteDataModule;
import com.canadainformation.data.remote.InformationRemoteDataSource;
import com.canadainformation.data.scopes.Local;
import com.canadainformation.data.scopes.Remote;
import com.canadainformation.di.scopes.AppScoped;
import com.canadainformation.provider.BaseSchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created By Akash
 * on 17,Dec,2019 : 6:25 PM
 */
@Module(includes = {InformationLocalDataModule.class, InformationRemoteDataModule.class})
public class MainRepositoryModule {

    @Provides
    @Local
    @AppScoped
    MainAppDataSource provideQuakesLocalDataSource(InformationDao quakesDao,
                                                   BaseSchedulerProvider schedulerProvider) {
        return new InformationLocalDataSource(quakesDao, schedulerProvider);
    }

    @Provides
    @Remote
    @AppScoped
    MainAppDataSource provideQuakesRemoteDataSource(InformationDataService apiService) {
        return new InformationRemoteDataSource(apiService);
    }

}
