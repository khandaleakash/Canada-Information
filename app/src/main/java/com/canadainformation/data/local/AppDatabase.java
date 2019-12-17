package com.canadainformation.data.local;

/**
 * Created By Akash
 * on 17,Dec,2019 : 4:04 PM
 */

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.canadainformation.model.Information;
import com.canadainformation.model.MainResponse;

/**
 * The Room Database that contains the Quakes table.
 */

@Database(entities = {Information.class, MainResponse.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    public abstract InformationDao informationDao();


}
