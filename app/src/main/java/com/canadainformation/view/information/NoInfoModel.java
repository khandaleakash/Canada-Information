package com.canadainformation.view.information;

/**
 * Created By Akash
 * on 17,Dec,2019 : 8:22 PM
 */

import androidx.annotation.StringRes;

/**
 * The string  that should be displayed when there are no Info.
 */
public final class NoInfoModel {
    @StringRes
    private int mText;

    public NoInfoModel(int mText) {
        this.mText = mText;
    }

    @StringRes
    public int getText() {
        return mText;
    }

    public void setText(int mText) {
        this.mText = mText;
    }
}
