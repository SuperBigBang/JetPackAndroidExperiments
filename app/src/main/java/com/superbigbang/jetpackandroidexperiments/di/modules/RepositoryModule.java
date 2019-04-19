package com.superbigbang.jetpackandroidexperiments.di.modules;

import com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.IssueRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    private IssueRepositoryImpl mIssueRepository;

    public RepositoryModule() {
        mIssueRepository = new IssueRepositoryImpl();
    }

    @Provides
    @Singleton
    public IssueRepositoryImpl provideIssueRepositoryImpl() {
        return mIssueRepository;
    }
}