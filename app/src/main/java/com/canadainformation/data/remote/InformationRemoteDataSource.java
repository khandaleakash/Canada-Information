package com.canadainformation.data.remote;

import androidx.annotation.NonNull;

import com.canadainformation.data.MainAppDataSource;
import com.canadainformation.model.Information;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created By Akash
 * on 17,Dec,2019 : 5:00 PM
 */
public class InformationRemoteDataSource implements MainAppDataSource {

    @NonNull
    private InformationDataService dataService;

    @Inject
    public InformationRemoteDataSource(@NonNull InformationDataService service) {
        dataService = service;
    }

    /**
     * Fresh items are retrieved from Remote API
     */
    @NonNull
    @Override
    public Single<List<Information>> getInformation() {
        return dataService.getMainInformation()
                .flatMap(response -> Observable.fromIterable(response.getInformationList()).toList())
                .flatMap(responseList -> Observable.fromIterable(responseList)
                        .map(information -> {
                            information.setTimeStampAdded(System.currentTimeMillis());
                            return information;
                        }).toList());


    }

    /**
     * These methods should be implemented when required
     * (e.g. when a cloud service is integrated)
     */

    @NonNull
    @Override
    public Single<Information> getInformation(@NonNull String informationTitle) {
        return null;
    }

    @Override
    public void saveInformation(@NonNull List<Information> information) {

    }

    @Override
    public void saveInformation(@NonNull Information information) {

    }

    @Override
    public void deleteAllInformation() {

    }

    @Override
    public void deleteInformation(@NonNull String informationTitle) {

    }
}
