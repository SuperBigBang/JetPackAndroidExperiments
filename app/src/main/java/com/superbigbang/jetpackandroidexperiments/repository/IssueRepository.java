package com.superbigbang.jetpackandroidexperiments.repository;

import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;

import androidx.lifecycle.LiveData;

public interface IssueRepository {
    LiveData<ApiResponse> getIssues(String owner, String repo);
}
