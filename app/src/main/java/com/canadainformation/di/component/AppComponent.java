package com.canadainformation.di.component;

import android.app.Application;

import com.canadainformation.MainApplication;
import com.canadainformation.data.MainRepositoryModule;
import com.canadainformation.di.module.ActivityBindingModule;
import com.canadainformation.di.module.AppModule;
import com.canadainformation.di.module.UtilsModule;
import com.canadainformation.di.module.ViewModelModule;
import com.canadainformation.di.scopes.AppScoped;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This is the root Dagger component.
 * {@link AndroidSupportInjectionModule}
 * is the module from Dagger.Android that helps with the generation
 * and location of subcomponents, which will be in our case, activities
 */
@AppScoped
@Component(modules = {MainRepositoryModule.class,
        ViewModelModule.class,
        UtilsModule.class,
        AppModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<MainApplication> {

    // we can now do DaggerAppComponent.builder().application(this).build().inject(this),
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}