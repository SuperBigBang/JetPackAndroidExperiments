package com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule;

import androidx.lifecycle.LiveData;

import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;

public interface IssueRepository {
    LiveData<ApiResponse> getIssues(String owner, String repo);
}
