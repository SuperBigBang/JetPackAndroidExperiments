package com.superbigbang.jetpackandroidexperiments;

import android.app.Application;

import com.superbigbang.jetpackandroidexperiments.di.BaseComponent;
import com.superbigbang.jetpackandroidexperiments.di.DaggerBaseComponent;
import com.superbigbang.jetpackandroidexperiments.di.modules.ContextModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.RepositoryModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.RoomModule;

import timber.log.Timber;


public class ExtendApplication extends Application {

    private static BaseComponent sBaseComponent;

    public static BaseComponent getBaseComponent() {
        return sBaseComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        sBaseComponent = DaggerBaseComponent.builder()
                .contextModule(new ContextModule(this))
                .repositoryModule(new RepositoryModule())
                .roomModule(new RoomModule(this))
                .build();
    }
}