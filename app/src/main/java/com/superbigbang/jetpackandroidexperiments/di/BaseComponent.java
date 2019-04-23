package com.superbigbang.jetpackandroidexperiments.di;

import android.content.Context;

import com.superbigbang.jetpackandroidexperiments.di.modules.ContextModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.RepositoryModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.RoomModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.IssueRepositoryImpl;
import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.AppDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, RepositoryModule.class, RoomModule.class})
public interface BaseComponent {
    Context getContext();

    IssueRepositoryImpl getIssueRepositoryImpl();

    AppDatabase getAppDatabase();
}
