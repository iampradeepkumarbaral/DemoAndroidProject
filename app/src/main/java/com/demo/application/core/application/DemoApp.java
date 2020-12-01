package com.demo.application.core.application;

import android.app.Application;
import android.content.Context;

import com.demo.application.BuildConfig;
import com.demo.application.core.application.di.component.AppComponent;

import com.demo.application.core.application.di.component.DaggerAppComponent;
import com.demo.application.core.application.di.module.AppModule;
import com.demo.application.core.application.di.module.UtilsModule;
import com.demo.application.home.data.model.AuroScholarDataModel;
import com.google.firebase.crashlytics.FirebaseCrashlytics;



public class DemoApp extends Application {

    AppComponent appComponent;
    public static DemoApp context;




    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .utilsModule(new UtilsModule())
                .build();

        appComponent.injectAppContext(this);
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        }

    }

    public AppComponent getAppComponent() {

        return appComponent;
    }

    public static DemoApp getAppContext() {

        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }




}
