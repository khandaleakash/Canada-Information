package com.canadainformation.data.local;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.canadainformation.model.Information;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.subscribers.TestSubscriber;


/**
 * Created By Akash
 * on 23,Dec,2019 : 6:23 PM
 */
@RunWith(AndroidJUnit4.class)
public class InformationDaoTest {

    private AppDatabase mDatabase;

    private TestSubscriber<Information> mInformationTestSubscriber;

    private TestSubscriber<List<Information>> mInformationListTestSubscriber;

    private static final List<Information> INFORMATION_LIST = Arrays.asList(
            new Information(1, "Title 1", "Description 1", "Image Url 1"),
            new Information(2, "Title 2", "Description 2", "Image Url 2"),
            new Information(3, "Title 3", "Description 3", "Image Url 3"));


    @Before
    public void initializeDB(){
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class).build();

        mInformationTestSubscriber = new TestSubscriber<>();
        mInformationListTestSubscriber = new TestSubscriber<>();
    }

    @After
    public void closeDB(){
        mDatabase.close();
    }


    /**
     * Test scenario states:
     * Upon insertion of a Information, the correct item is retrieved
     */
    @Test
    public void insertInformationAndGetById() {
        // insert Information
        mDatabase.informationDao().insertInformation(INFORMATION_LIST.get(0));

        // getting the Information  by Id from the database
        mDatabase.informationDao()
                .getInformationById(INFORMATION_LIST.get(0).getId()).toFlowable().subscribe(mInformationTestSubscriber);

        // the loaded data contains the expected values
        mInformationTestSubscriber.assertValue(INFORMATION_LIST.get(0));
    }

    /**
     * Test scenario states:
     * Upon insertion of Information List, the correct list is retrieved
     */
    @Test
    public void insertInformationListAndGet() {
        // insert Information List
        mDatabase.informationDao().insertInformation(INFORMATION_LIST.get(0));
        mDatabase.informationDao().insertInformation(INFORMATION_LIST.get(1));
        mDatabase.informationDao().insertInformation(INFORMATION_LIST.get(2));


        // getting Information from the database
        mDatabase.informationDao()
                .getInformation().toFlowable().subscribe(mInformationListTestSubscriber);

        // the loaded data contains the expected values
        mInformationListTestSubscriber.assertValue(INFORMATION_LIST);
    }

    /**
     * Test scenario states:
     * Upon insertion of a conflictual Information, the latter one should be retrieved
     */
    @Test
    public void insertInformationAndReplaceOnConflict() {
        Information conflictualInformation =  new Information(1, "Title 111", "Description 1", "Image Url sas");

        // insert  initial Information
        mDatabase.informationDao().insertInformation(INFORMATION_LIST.get(0));

        // insert  Information
        mDatabase.informationDao().insertInformation(conflictualInformation);


        // getting Information from the database
        mDatabase.informationDao()
                .getInformationById(1).toFlowable().subscribe(mInformationTestSubscriber);

        // the loaded data contains the expected values
        mInformationTestSubscriber.assertValue(conflictualInformation);
    }

    /**
     * Test scenario states:
     * Upon insertion of 1 Information  and deletion of all records, we should retrieve an empty list
     * fom the database
     */
    @Test
    public void deleteInformationAndGetInformationList_Scenario() {
        // given a Information (item) inserted
        mDatabase.informationDao().insertInformation(INFORMATION_LIST.get(0));

        // deleting all data
        mDatabase.informationDao().deleteInformation();

        // getting the data
        mDatabase.informationDao().getInformation().toFlowable().subscribe(mInformationListTestSubscriber);

        // the list should be empty
        mInformationListTestSubscriber.assertValue(new ArrayList<>());
    }
}