package com.canadainformation.di.module;

import android.app.Application;
import android.content.Context;

import com.canadainformation.di.component.AppComponent;

import dagger.Binds;
import dagger.Module;

/**
 * Created By Akash
 * on 18,Dec,2019 : 11:38 AM
 */

/**
 * This is the app's Dagger module. We use this to bind our Application class as a Context in the AppComponent.
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 * through Dagger.Android our Application & Activities are provided into your graph for us.
 * {@link
 * AppComponent}.
 */
@Module
public abstract class AppModule {
    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);
}