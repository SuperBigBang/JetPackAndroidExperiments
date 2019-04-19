package com.superbigbang.jetpackandroidexperiments.di;

import android.content.Context;

import com.superbigbang.jetpackandroidexperiments.di.modules.ContextModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.RepositoryModule;
import com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.IssueRepositoryImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, RepositoryModule.class})
public interface BaseComponent {
    Context getContext();

    IssueRepositoryImpl getIssueRepositoryImpl();
}
