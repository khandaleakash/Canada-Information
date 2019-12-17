package com.canadainformation.data;

import androidx.annotation.NonNull;

import com.canadainformation.data.scopes.Local;
import com.canadainformation.data.scopes.Remote;
import com.canadainformation.model.Information;
import com.canadainformation.utils.NetworkWatcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created By Akash
 * on 17,Dec,2019 : 3:31 PM
 */
public class MainDataRepository implements MainAppDataSource {

    private final MainAppDataSource mInformationRemoteDataSource;

    private final MainAppDataSource mInformationLocalDataSource;

    private final NetworkWatcher mNetworkWatcher;

    /**
     * Dagger allows us to have a single instance of the repository throughout the app
     *
     * @param informationRemoteDataSource the backend data source (Remote Source)
     * @param informationLocalDataSource  the device storage data source (Local Source)
     */
    @Inject
    public MainDataRepository(@Remote MainAppDataSource informationRemoteDataSource,
                              @Local MainAppDataSource informationLocalDataSource,
                              NetworkWatcher networkWatcher) {
        mInformationRemoteDataSource = informationRemoteDataSource;
        mInformationLocalDataSource = informationLocalDataSource;
        mNetworkWatcher = networkWatcher;
    }

    /**
     * The retrieval logic sets the Local Source as the primary source
     * In case of an active internet connection and the absence of Local database
     * or if it contains stale data, the Remote Source is queried and the Local one is refreshed
     */

    @NonNull
    @Override
    public Single<List<Information>> getInformation() {
        return mInformationLocalDataSource.getInformation().
                flatMap(data -> {
                    if (mNetworkWatcher.isNetworkAvailable() && (data.isEmpty() || isStale(data))) {
                        return getFreshInformation();
                    }

                    return Single.just(data);
                });
    }

    @NonNull
    @Override
    public Single<Information> getInformation(@NonNull String informationTitle) {
        checkNotNull(informationTitle);
        return mInformationLocalDataSource.getInformation(informationTitle);
    }

    @Override
    public void saveInformation(@NonNull List<Information> information) {
        checkNotNull(information);
        mInformationLocalDataSource.saveInformation(information);
        mInformationRemoteDataSource.saveInformation(information);
    }

    @Override
    public void saveInformation(@NonNull Information information) {
        checkNotNull(information);
        mInformationLocalDataSource.saveInformation(information);
        mInformationRemoteDataSource.saveInformation(information);

    }

    @Override
    public void deleteAllInformation() {
        mInformationLocalDataSource.deleteAllInformation();
        mInformationRemoteDataSource.deleteAllInformation();
    }

    @Override
    public void deleteInformation(@NonNull String informationTitle) {
        checkNotNull(informationTitle);
        mInformationLocalDataSource.deleteInformation(informationTitle);
        mInformationRemoteDataSource.deleteInformation(informationTitle);
    }


    /**
     * Helper methods, should be encapsulated
     */

    private boolean isStale(List<Information> data) {
        // it is enough for 1 item to be stale
        return !data.get(0).isUpdated();
    }

    /**
     * Contains data refreshing logic
     * Both sources are emptied, then new items are retrieved from querying the Remote Source
     * and finally, sources are replenished
     */
    private Single<List<Information>> getFreshInformation() {
        deleteAllInformation();

        return mInformationRemoteDataSource.getInformation().
                doOnSuccess(this::saveInformation);
    }
}
