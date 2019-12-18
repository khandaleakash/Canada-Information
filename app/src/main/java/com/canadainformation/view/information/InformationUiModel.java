package com.canadainformation.view.information;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created By Akash
 * on 17,Dec,2019 : 8:26 PM
 */
public class InformationUiModel {

    private final boolean mIsNoInfoAvailable;

    private final List<InfoItem> mItemList;

    private final boolean mIsNoInfoViewVisible;

    private final NoInfoModel mNoInfoModel;


    public InformationUiModel(boolean isNoInfoAvailable, List<InfoItem> itemList,
                              boolean isNoInfoViewVisible, NoInfoModel noInfoModel) {
        mIsNoInfoAvailable = isNoInfoAvailable;
        mItemList = itemList;
        mIsNoInfoViewVisible = isNoInfoViewVisible;
        mNoInfoModel = noInfoModel;
    }

    public boolean ismIsNoInfoAvailable() {
        return mIsNoInfoAvailable;
    }

    public List<InfoItem> getmItemList() {
        return mItemList;
    }

    public boolean ismIsNoInfoViewVisible() {
        return mIsNoInfoViewVisible;
    }

    @Nullable
    public NoInfoModel getmNoInfoModel() {
        return mNoInfoModel;
    }
}
