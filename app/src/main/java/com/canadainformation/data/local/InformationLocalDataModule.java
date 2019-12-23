package com.canadainformation.data.local;

import android.app.Application;

import androidx.room.Room;

import com.canadainformation.di.scopes.AppScoped;
import com.canadainformation.utils.Constants;

import dagger.Module;
import dagger.Provides;

/**
 * Created By Akash
 * on 17,Dec,2019 : 4:57 PM
 */
@Module
public class InformationLocalDataModule {
    @AppScoped
    @Provides
    AppDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, Constants.INFORMATION_DB)
                .build();
    }

    @AppScoped
    @Provides
    InformationDao provideInformationDao(AppDatabase db) {
        return db.informationDao();
    }
}
