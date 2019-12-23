package com.canadainformation.data.remote;

import com.canadainformation.model.Information;
import com.canadainformation.model.MainResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created By Akash
 * on 23,Dec,2019 : 9:50 PM
 */
public class InformationRemoteDataSourceTest {

    @Mock
    InformationDataService mInformationDataService;

    private InformationRemoteDataSource mRemoteDataSource;

    @Before
    public void setup() throws Exception {
        // init mocks
        MockitoAnnotations.initMocks(this);

        // get reference to the class in test
        mRemoteDataSource = new InformationRemoteDataSource(mInformationDataService);

    }

    @Test
    public void testPreConditions() {
        assertNotNull(mRemoteDataSource);
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct results in success scenario
     */
    @Test
    public void testRemoteApiResponse_success() throws Exception {
        TestSubscriber<List<Information>> testSubscriber = new TestSubscriber<>();

        List<Information> listInformation = new ArrayList<>();

        // set up mock response
        MainResponse mockInformationResponse = new MainResponse();
        Information tempInformation = new Information(123,"Title Temp", "Description Temp","Image Url Temp");
        listInformation.add(tempInformation);

        mockInformationResponse.setTitle("Main Title Temp");
        mockInformationResponse.setInformationList(listInformation);

        // prepare fake response
        when(mInformationDataService.getMainInformation())
                .thenReturn(Single.just(mockInformationResponse));

        // trigger response
        mRemoteDataSource.getInformation().toFlowable().subscribe(testSubscriber);

        List<Information> result = testSubscriber.values().get(0);

        testSubscriber.assertValue(listInformation);
    }

    /**
     * Test scenario states:
     * Remote Source should get the correct results in failure scenario
     */
    @Test
    public void testRemoteApiResponse_failure() throws Exception {
        TestSubscriber<List<Information>> testSubscriber = new TestSubscriber<>();

        // prepare fake exception
        Throwable exception = new IOException();

        // prepare fake response
        when(mInformationDataService.getMainInformation()).
                thenReturn(Single.error(exception));

        // assume the repository calls the remote DataSource
        mRemoteDataSource.getInformation().toFlowable().subscribe(testSubscriber);

        testSubscriber.assertError(IOException.class);
    }


}