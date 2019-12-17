package com.canadainformation.data.remote;

import com.canadainformation.model.MainResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created By Akash
 * on 17,Dec,2019 : 4:16 PM
 */
public interface InformationDataService {

    @GET("facts.json")
    Single<MainResponse> getMainInformation();
}
