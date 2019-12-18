package com.canadainformation.provider;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.common.base.Preconditions;

/**
 * Created By Akash
 * on 17,Dec,2019 : 8:32 PM
 */

/**
 * Concrete implementation of the {@link BaseResourceProvider} interface.
 */
public class ResourceProvider implements BaseResourceProvider {

    @NonNull
    private final Context mContext;

    public ResourceProvider(@NonNull Context context) {
        mContext = Preconditions.checkNotNull(context, "context cannot be null");
    }

    @NonNull
    @Override
    public String getString(@StringRes final int id) {
        return mContext.getString(id);
    }

    @NonNull
    @Override
    public String getString(@StringRes final int id, final Object... formatArgs) {
        return mContext.getString(id, formatArgs);
    }
}

