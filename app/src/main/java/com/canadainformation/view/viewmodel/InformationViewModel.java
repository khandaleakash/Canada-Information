package com.canadainformation.view.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.canadainformation.R;
import com.canadainformation.data.MainDataRepository;
import com.canadainformation.di.scopes.AppScoped;
import com.canadainformation.model.Information;
import com.canadainformation.view.information.InfoItem;
import com.canadainformation.view.information.InformationUiModel;
import com.canadainformation.view.information.NoInfoModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:59 PM
 */

/**
 * ViewModel for Information screen
 */
@AppScoped
public class InformationViewModel extends ViewModel {

    @NonNull
    private final MainDataRepository mainDataRepository;
    // using a BehaviourSubject because we are interested in the last object that was emitted before
    // subscribing. Like this we ensure that the loading indicator has the correct visibility.
    private final BehaviorSubject<Boolean> mLoadingIndicatorSubject;

    // using a PublishSubject because we are not interested in the last object that was emitted
    // before subscribing. Like this we avoid displaying the snackbar multiple times
    @NonNull
    private final PublishSubject<Integer> mSnackbarText;

    @Inject
    public InformationViewModel(@NonNull MainDataRepository repository) {
        mainDataRepository = repository;

        mLoadingIndicatorSubject = BehaviorSubject.createDefault(false);
        mSnackbarText = PublishSubject.create();
    }


    /**
     * @return the model for the quakes screen
     */
    @NonNull
    public Single<InformationUiModel> getUiModel(boolean isForcedCall) {
        return getInfoItems(isForcedCall)
                .doOnSubscribe(__ -> mLoadingIndicatorSubject.onNext(true))
                .doOnSuccess(__ -> mLoadingIndicatorSubject.onNext(false))
                .doOnError(__ -> mSnackbarText.onNext(R.string.loading_information_error))
                .map(this::constructInfoModel);
    }

    /**
     * @return a list of items that should be displayed to the user
     * Contains force update logic - if user forces update, the Sources are emptied, therefore
     * forcing the repository to emit fresh items from the Remote Source
     */
    private Single<List<InfoItem>> getInfoItems(boolean isForcedCall) {
        // TODO find better optimized mechanism to triggering remote data retrieval
        if (isForcedCall) mainDataRepository.deleteAllInformation();

        return mainDataRepository.getInformation()
                .flatMap(list -> Observable.fromIterable(list)
                        .map(this::constructInfoItem).toList());
    }

    @NonNull
    private InformationUiModel constructInfoModel(List<InfoItem> quakes) {
        boolean mIsNoInfoAvailable = !quakes.isEmpty();
        boolean mIsNoInfoViewVisible = !mIsNoInfoAvailable;
        NoInfoModel noInfoModel = null;
        if (quakes.isEmpty()) {
            noInfoModel = getNoInfoModel();
        }

        return new InformationUiModel(mIsNoInfoAvailable, quakes, mIsNoInfoViewVisible,
                noInfoModel);
    }

    private NoInfoModel getNoInfoModel() {
        return new NoInfoModel(R.string.no_info);
    }

    private InfoItem constructInfoItem(Information information) {
        return new InfoItem(information, () -> handleInfoClicked(information));
    }

    private void handleInfoClicked(Information information) {


    }

    /**
     * @return a stream of ids that should be displayed in the snackbar
     */
    @NonNull
    public Observable<Integer> getSnackbarMessage() {
        return mSnackbarText.hide();
    }

    /**
     * @return a stream that emits true if the progress indicator should be displayed, false otherwise
     */
    @NonNull
    public Observable<Boolean> getLoadingIndicatorVisibility() {
        return mLoadingIndicatorSubject.hide();
    }
}
