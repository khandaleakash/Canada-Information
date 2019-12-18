package com.canadainformation.schedulers;

/**
 * Created By Akash
 * on 17,Dec,2019 : 4:20 PM
 */

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 */
public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
