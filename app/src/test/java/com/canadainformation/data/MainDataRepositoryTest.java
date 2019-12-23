package com.canadainformation.data;

import com.canadainformation.model.Information;
import com.canadainformation.utils.NetworkWatcher;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created By Akash
 * on 23,Dec,2019 : 9:58 PM
 */

/**
 * Unit tests for the implementation of the repository
 */
public class MainDataRepositoryTest {

    private long testTime = System.currentTimeMillis();
    private long staleTime = 6 * 60 * 1000;

    private List<Information> INFORMATION_RECENT = Lists.newArrayList(
            new Information(111, "Title 111", "Description 111", "Image Url 111", testTime),
            new Information(112, "Title 112", "Description 112", "Image Url 112", testTime));
    private List<Information> INFORMATION_STALE = Lists.newArrayList(
            new Information(111, "Title 111", "Description 111", "Image Url 111", testTime - staleTime),
            new Information(112, "Title 112", "Description 112", "Image Url 112", testTime - staleTime));

    private MainDataRepository mMainDataRepository;

    private TestSubscriber<List<Information>> mInformationTestSubscriber;

    @Mock
    private MainAppDataSource mMainAppRemoteDataSource;

    @Mock
    private MainAppDataSource mMainAppLocalDataSource;
    @Mock
    private NetworkWatcher mNetworkWatcher;


    @Before
    public void setupRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mMainDataRepository = new MainDataRepository
                (mMainAppRemoteDataSource, mMainAppLocalDataSource, mNetworkWatcher);

        mInformationTestSubscriber = new TestSubscriber<>();
    }


    /**
     * Offline Test scenario states:
     * As the disk has up-to-date items, upon querying the repository with no internet connection,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getInformationOffline_requestsInformationFromLocalDataSource() {

        // the local data source has up-to-date data available
        new ArrangeBuilder()
                .withInformationAvailable(mMainAppLocalDataSource, INFORMATION_RECENT);

        // establish a fake internet connection status
        when(mNetworkWatcher.isNetworkAvailable()).thenReturn(false);

        // When Information are requested from the Information repository
        mMainDataRepository.getInformation().toFlowable().subscribe(mInformationTestSubscriber);

        // Then Information are loaded from the local data source
        verify(mMainAppLocalDataSource).getInformation();
        mInformationTestSubscriber.assertValue(INFORMATION_RECENT);
    }

    /**
     * Online Test scenario states:
     * As the disk has up-to-date items, upon querying the repository with active internet connection,
     * the Local Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getInformationOnline_requestsInformationFromLocalDataSource_upToDateLocal() {

        // the local data source has up-to-date data available
        new ArrangeBuilder()
                .withInformationAvailable(mMainAppLocalDataSource, INFORMATION_RECENT);

        // establish a fake internet connection status
        when(mNetworkWatcher.isNetworkAvailable()).thenReturn(true);

        // When Information are requested from the Information repository
        mMainDataRepository.getInformation().toFlowable().subscribe(mInformationTestSubscriber);

        // Then Information are loaded from the local data source
        verify(mMainAppLocalDataSource).getInformation();
        mInformationTestSubscriber.assertValue(INFORMATION_RECENT);
    }


    /**
     * Online Test scenario states:
     * As the disk has stale items, upon querying the repository with active internet connection,
     * the Remote Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getInformationOnline_requestsInformationFromRemoteDataSource_staleLocal() {

        // the remote data source has fresh data available
        new ArrangeBuilder()
                .withInformationAvailable(mMainAppRemoteDataSource, INFORMATION_RECENT);

        // the local data source has stale data available
        new ArrangeBuilder()
                .withInformationAvailable(mMainAppLocalDataSource, INFORMATION_RECENT);

        // establish a fake internet connection status
        when(mNetworkWatcher.isNetworkAvailable()).thenReturn(true);

        // When Information are requested from the Information repository
        mMainDataRepository.getInformation().toFlowable().subscribe(mInformationTestSubscriber);

        // Both sources should be queried, yet the local source has stale items
        // which triggers the call to the remote source
        verify(mMainAppRemoteDataSource).getInformation();
        verify(mMainAppLocalDataSource).getInformation();

        mInformationTestSubscriber.assertValue(INFORMATION_RECENT);
    }

    /**
     * Online Test scenario states:
     * As the disk has no items, upon querying the repository with active internet connection,
     * the Remote Data Source should be accessed and correct items should be retrieved
     */
    @Test
    public void getInformationOnline_requestsInformationFromRemoteDataSource_emptyLocal() {

        // the remote data source has fresh data available
        new ArrangeBuilder()
                .withInformationAvailable(mMainAppRemoteDataSource, INFORMATION_RECENT);

        // the local data source has stale data available
        new ArrangeBuilder()
                .withInformationNotAvailable(mMainAppLocalDataSource);

        // establish a fake internet connection status
        when(mNetworkWatcher.isNetworkAvailable()).thenReturn(true);

        // When Information are requested from the Information repository
        mMainDataRepository.getInformation().toFlowable().subscribe(mInformationTestSubscriber);

        // Both sources should be queried, yet the local source has no items
        // which triggers the call to the remote source
        verify(mMainAppLocalDataSource).getInformation();
        verify(mMainAppRemoteDataSource).getInformation();

        mInformationTestSubscriber.assertValue(INFORMATION_RECENT);
    }

    /**
     * Test scenario states:
     * Upon get command , both Local Data Source should retrieve the item locally
     */
    @Test
    public void getInformationFromLocal() {
        Information tempInformation = new Information(11, "Title Temp", "Description Temp", "Image Url Temp");

        new ArrangeBuilder().withInformationByTitle(mMainAppLocalDataSource, tempInformation);

        mMainDataRepository.getInformation(tempInformation.getTitle()).toFlowable().subscribe();

        // upon get command, check if only local data source is being called
        verify(mMainAppLocalDataSource).getInformation(tempInformation.getTitle());
        verify(mMainAppRemoteDataSource, never()).getInformation(tempInformation.getTitle());
    }

    /**
     * Test scenario states:
     * Upon save command , both Local Data Source and Remote Data Source should save
     * the corresponding items taken as parameter
     */
    @Test
    public void saveInformationList() {
        mMainDataRepository.saveInformation(INFORMATION_RECENT);

        // upon save command, check if both data sources are being called
        verify(mMainAppLocalDataSource).saveInformation(INFORMATION_RECENT);
        verify(mMainAppRemoteDataSource).saveInformation(INFORMATION_RECENT);
    }

    /**
     * Test scenario states:
     * Upon save command , both Local Data Source and Remote Data Source should save
     * the corresponding item taken as parameter
     */
    @Test
    public void saveInformation() {
        mMainDataRepository.saveInformation(INFORMATION_RECENT.get(0));

        // upon save command, check if both data sources are being called
        verify(mMainAppLocalDataSource).saveInformation(INFORMATION_RECENT.get(0));
        verify(mMainAppRemoteDataSource).saveInformation(INFORMATION_RECENT.get(0));
    }

    /**
     * Test scenario states:
     * Upon delete command , both Local Data Source and Remote Data Source should delete
     * all items
     */
    @Test
    public void deleteInformationList() {
        mMainDataRepository.deleteAllInformation();

        // upon save command, check if both data sources are being called
        verify(mMainAppLocalDataSource).deleteAllInformation();
        verify(mMainAppRemoteDataSource).deleteAllInformation();
    }

    /**
     * Test scenario states:
     * Upon delete command , both Local Data Source and Remote Data Source should delete
     * the corresponding item
     */
    @Test
    public void deleteInformation() {
        mMainDataRepository.deleteInformation("Temp Title");

        // upon save command, check if both data sources are being called
        verify(mMainAppLocalDataSource).deleteInformation("Temp Title");
        verify(mMainAppRemoteDataSource).deleteInformation("Temp Title");
    }

    class ArrangeBuilder {

        ArrangeBuilder withInformationNotAvailable(MainAppDataSource mainAppDataSource) {
            when(mainAppDataSource.getInformation()).thenReturn(Single.just(Collections.emptyList()));
            return this;
        }

        ArrangeBuilder withInformationAvailable(MainAppDataSource mainAppDataSource, List<Information> informationList) {
            // don't allow the data sources to complete.
            when(mainAppDataSource.getInformation()).thenReturn(Single.just(informationList));
            return this;
        }

        ArrangeBuilder withInformationByTitle(MainAppDataSource mainAppDataSource, Information information) {
            // don't allow the data sources to complete.
            when(mainAppDataSource.getInformation(information.getTitle())).thenReturn(Single.just(information));
            return this;
        }
    }
}