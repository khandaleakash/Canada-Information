package com.canadainformation.data.local;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.canadainformation.model.Information;
import com.canadainformation.schedulers.BaseSchedulerProvider;
import com.canadainformation.schedulers.ImmediateSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

/**
 * Created By Akash
 * on 23,Dec,2019 : 9:36 PM
 */
@RunWith(AndroidJUnit4.class)
public class InformationLocalDataSourceTest {

    private InformationLocalDataSource mLocalDataSource;

    private AppDatabase mDatabase;

    private BaseSchedulerProvider mSchedulerProvider;

    private static final Information INFORMATION = new Information(1, "Title 1", "Description 1", "Image Url 1");

    @Before
    public void initialize() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class)
                .build();
        InformationDao informationDao = mDatabase.informationDao();

        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Make sure that we're not keeping a reference to the wrong instance.
        mLocalDataSource = new InformationLocalDataSource(informationDao, mSchedulerProvider);
    }

    @After
    public void clear() {
        mDatabase.informationDao().deleteInformation();
        mDatabase.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon saving a Information
     */
    @Test
    public void saveInformation_retrievesInformation() {
        // When saved into the Information repository
        mLocalDataSource.saveInformation(INFORMATION);

        // Then the Information can be retrieved from the persistent repository
        TestSubscriber<Information> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getInformation(INFORMATION.getTitle()).toFlowable().subscribe(testSubscriber);

        testSubscriber.assertValue(INFORMATION);
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon saving Information
     */
    @Test
    public void getInformation_retrieveSavedInformation() {
        // Given 2 new Information in the persistent repository
        final Information newInformation = new Information(2, "Title 2", "Description 2", "Image Url 2");
        mLocalDataSource.saveInformation(newInformation);

        final Information newInformation2 = new Information(3, "Title 3", "Description 3", "Image Url 3");

        mLocalDataSource.saveInformation(newInformation2);

        // Then the Information List can be retrieved from the persistent repository
        TestSubscriber<List<Information>> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getInformation().toFlowable().subscribe(testSubscriber);

        List<Information> result = testSubscriber.values().get(0);

        assertThat(result, hasItems(newInformation, newInformation2));
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon having no data
     */
    @Test
    public void getInformation_whenInformationNotSaved() {
        //Given that no Information has been saved
        //When querying for a Information, no values are returned.
        TestSubscriber<Information> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getInformation("some_title").toFlowable().subscribe(testSubscriber);

        testSubscriber.assertNoValues();
    }

    /**
     * Test scenario states:
     * Local Source should get the correct result upon emptying source
     */
    @Test
    public void deleteAllInformation_emptyListOfRetrievedInformation() {
        // Given a new Information in the persistent repository
        mLocalDataSource.saveInformation(INFORMATION);

        // When all Information List is deleted
        mLocalDataSource.deleteAllInformation();

        // Then the retrieved Information List is an empty list
        TestSubscriber<List<Information>> testSubscriber = new TestSubscriber<>();

        mLocalDataSource.getInformation().toFlowable().subscribe(testSubscriber);

        List<Information> result = testSubscriber.values().get(0);

        assertThat(result.size(), is(0));
    }


}