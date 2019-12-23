package com.canadainformation.data;

import androidx.annotation.NonNull;

import com.canadainformation.model.Information;

import java.util.List;

import io.reactivex.Single;

/**
 * Created By Akash
 * on 17,Dec,2019 : 3:30 PM
 */
public interface MainAppDataSource {
    @NonNull
    Single<List<Information>> getInformation();

    @NonNull
    Single<Information> getInformation(@NonNull String informationTitle);

    @NonNull
    Single<String> getInformationTitle();

    void saveInformation(@NonNull List<Information> information);

    void saveInformation(@NonNull Information information);

    void deleteAllInformation();

    void deleteInformation(@NonNull String informationTitle);


}
