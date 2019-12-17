package com.canadainformation.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.canadainformation.model.Information;

import java.util.List;

import io.reactivex.Single;

/**
 * Created By Akash
 * on 17,Dec,2019 : 3:36 PM
 */
@Dao
public interface InformationDao {

    @Query("SELECT * FROM information ")
    Single<List<Information>> getInformation();


    /**
     * Retrieve a Information by title.
     *
     * @param title the Information title.
     * @return the information with title
     */
    @Query("SELECT * FROM information WHERE title = :title")
    Single<Information> getInformationByTitle(String title);



    /**
     * Insert Information in the database. If the Information already exists, ignore the action.
     *
     * @param information to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInformation(Information information);

    /**
     * Delete a Information by title.
     *
     * @return the number of Information deleted. This should always be 1.
     */
    @Query("DELETE FROM information WHERE title = :title")
    int deleteInformationByTitle(String title);


    /**
     * Delete all Information (items).
     */
    @Query("DELETE FROM information")
    void deleteInformation();

    @Update
    int updateInformation(Information information);
}
