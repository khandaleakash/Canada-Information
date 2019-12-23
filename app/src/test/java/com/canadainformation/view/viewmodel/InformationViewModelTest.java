package com.canadainformation.view.viewmodel;

import com.canadainformation.R;
import com.canadainformation.data.MainDataRepository;
import com.canadainformation.model.Information;
import com.canadainformation.view.information.InfoItem;
import com.canadainformation.view.information.InformationUiModel;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created By Akash
 * on 24,Dec,2019 : 12:45 AM
 */

/**
 * Unit test for {@link InformationViewModel}
 */
public class InformationViewModelTest {

    private static List<Information> INFORMATION_LIST;

    private InformationViewModel mViewModel;

    private TestObserver<InformationUiModel> mInformationSubscriber;

    private TestObserver<Boolean> mProgressIndicatorSubscriber;

    private TestObserver<Integer> mSnackbarTextSubscriber;

    @Mock
    private MainDataRepository mInformaitionRepository;

    @Before
    public void setupInformationViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mViewModel = new InformationViewModel(mInformaitionRepository);


        INFORMATION_LIST = Lists.newArrayList(new Information(1, "Title 1", "Description 1", "Image Url 1"),
                new Information(2, "Title 2", "Description 2", "Image Url 2"));

        mInformationSubscriber = new TestObserver<>();
        mProgressIndicatorSubscriber = new TestObserver<>();
        mSnackbarTextSubscriber = new TestObserver<>();
    }

    @Test
    public void progressIndicator_emits_whenSubscribedToData() {
        // Given that the Information repository never emits
        when(mInformaitionRepository.getInformation()).thenReturn(Single.never());

        // Given that we are subscribed to the progress indicator
        mProgressIndicatorSubscriber = mViewModel.getLoadingIndicatorVisibility().test();

        // When subscribed to the Information model
        mViewModel.getUiModel(false).subscribe();

        // The progress indicator emits initially false and then true
        mProgressIndicatorSubscriber.assertValues(false, true);
    }

    @Test
    public void snackbarText_emits_whenError_whenRetrievingData() {
        // Given an error when retrieving Information
        when(mInformaitionRepository.getInformation()).thenReturn(Single.error(new RuntimeException()));

        // Given that we are subscribed to the snackbar text
        mViewModel.getSnackbarMessage().subscribe(mSnackbarTextSubscriber);

        // When subscribed to the Information model
        mViewModel.getUiModel(false).subscribe(mInformationSubscriber);

        // The snackbar emits an error message
        mSnackbarTextSubscriber.assertValue(R.string.loading_information_error);
    }

    @Test
    public void getInformationModel_emits_whenInformation() {
        // Given that we are subscribed to the emissions of the UI model
        withInformationListInRepositoryAndSubscribed(INFORMATION_LIST);

        // The Information model containing the list of Information is emitted
        mInformationSubscriber.assertValueCount(1);
        InformationUiModel model = mInformationSubscriber.values().get(0);
        assertInformationModelWithInformationVisible(model);
    }


    @Test
    public void forceUpdateInformation_updatesInformationRepository() {
        // Given that the Information repository never emits
        when(mInformaitionRepository.getInformation()).thenReturn(Single.never());

        // When calling force update
        mViewModel.getUiModel(true);

        // The Information are refreshed in the repository
        verify(mInformaitionRepository).deleteAllInformation();
    }

    @Test
    public void InformationItem_tapAction_opensInformationDetails() {
        Information tempInformation = new Information(11, "Title Temp", "Description Temp", "Image Url Temp");

        // Given a Information
        withInformationInRepositoryAndSubscribed(tempInformation);
        // And list of Information items is emitted
        List<InfoItem> items = mInformationSubscriber.values().get(0).getmItemList();
        InfoItem infoItem = items.get(0);

        // When triggering the click action
        infoItem.getOnClickAction().call();

    }


    private void assertInformationModelWithInformationVisible(InformationUiModel model) {
        assertTrue(model.ismIsNoInfoAvailable());
        assertInfoItems(model.getmItemList());
        assertFalse(model.ismIsNoInfoViewVisible());
        assertNull(model.getmNoInfoModel());
    }


    private void withInformationListInRepositoryAndSubscribed(List<Information> informationList) {
        // Given that the Information repository returns Information
        when(mInformaitionRepository.getInformation()).thenReturn(Single.just(informationList));

        // Given that we are subscribed to the Information
        mViewModel.getUiModel(false).subscribe(mInformationSubscriber);
    }

    private void withInformationInRepositoryAndSubscribed(Information information) {
        List<Information> informationList = new ArrayList<>();
        informationList.add(information);
        withInformationListInRepositoryAndSubscribed(informationList);
    }


    private void assertInfoItems(List<InfoItem> items) {
        // check if the InfoItems are the expected ones
        assertEquals(items.size(), INFORMATION_LIST.size());

        assertInformation(items.get(0), INFORMATION_LIST.get(0));
        assertInformation(items.get(1), INFORMATION_LIST.get(1));
    }

    private void assertInformation(InfoItem infoItem, Information information) {
        assertEquals(infoItem.getmInformation(), information);
        assertNotNull(infoItem.getOnClickAction());
    }
}