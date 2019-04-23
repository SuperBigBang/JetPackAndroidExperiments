package com.superbigbang.jetpackandroidexperiments.di.modules;

import android.content.Context;

import androidx.room.Room;

import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private AppDatabase mAppDatabase;

    public RoomModule(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "issuesDatabase").build();
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase() {
        return mAppDatabase;
    }
}