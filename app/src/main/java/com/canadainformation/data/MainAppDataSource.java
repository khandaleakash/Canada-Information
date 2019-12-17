package com.canadainformation.data;

import androidx.annotation.NonNull;

import com.canadainformation.model.Information;
import com.canadainformation.model.MainResponse;

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

    void saveInformation(@NonNull List<Information> information);

    void saveInformation(@NonNull Information information);

    void deleteAllInformation();

    void deleteInformation(@NonNull String informationTitle);


}
