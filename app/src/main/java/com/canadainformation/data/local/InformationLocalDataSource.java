package com.canadainformation.data.local;

import androidx.annotation.NonNull;

import com.canadainformation.data.MainAppDataSource;
import com.canadainformation.model.Information;
import com.canadainformation.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created By Akash
 * on 17,Dec,2019 : 4:18 PM
 */
public class InformationLocalDataSource implements MainAppDataSource {

    private final InformationDao mInformationDao;

    private BaseSchedulerProvider mSchedulerProvider;

    @Inject
    public InformationLocalDataSource(@NonNull InformationDao informationDao,
                                 @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(schedulerProvider, "scheduleProvider cannot be null");
        checkNotNull(informationDao, "informationDao cannot be null");

        mInformationDao = informationDao;
        mSchedulerProvider = schedulerProvider;
    }

    @NonNull
    @Override
    public Single<List<Information>> getInformation() {
        return mInformationDao.getInformation();
    }

    @NonNull
    @Override
    public Single<Information> getInformation(@NonNull String informationTitle) {
        return mInformationDao.getInformationByTitle(informationTitle);
    }

    @Override
    public void saveInformation(@NonNull List<Information> information) {
        checkNotNull(information);
        for (Information info:information)
            saveInformation(info);

    }

    @Override
    public void saveInformation(@NonNull Information information) {
        checkNotNull(information);
        Completable.fromRunnable(()->mInformationDao.insertInformation(information)).subscribeOn(mSchedulerProvider.io()).subscribe();
    }

    @Override
    public void deleteAllInformation() {
        Completable.fromRunnable(mInformationDao::deleteInformation)
                .subscribeOn(mSchedulerProvider.io()).subscribe();
    }

    @Override
    public void deleteInformation(@NonNull String informationTitle) {
        Completable.fromRunnable(()->mInformationDao.deleteInformationByTitle(informationTitle))
                .subscribeOn(mSchedulerProvider.io()).subscribe();

    }

    @NonNull
    @Override
    public Single<String> getInformationTitle() {
        return null;
    }
}
