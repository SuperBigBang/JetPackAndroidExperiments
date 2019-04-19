package com.superbigbang.jetpackandroidexperiments.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;
import com.superbigbang.jetpackandroidexperiments.repository.IssueRepository;
import com.superbigbang.jetpackandroidexperiments.repository.IssueRepositoryImpl;

public class MainActivityViewModel extends ViewModel {

    private MediatorLiveData<ApiResponse> mApiResponse;
    private IssueRepository mIssueRepository;

    // No argument constructor
    public MainActivityViewModel() {
        mApiResponse = new MediatorLiveData<>();
        mIssueRepository = new IssueRepositoryImpl();
    }

    @NonNull
    public LiveData<ApiResponse> getApiResponse() {
        return mApiResponse;
    }

    public LiveData<ApiResponse> loadIssues(@NonNull String user, String repo) {
        mApiResponse.addSource(
                mIssueRepository.getIssues(user, repo),
                apiResponse -> mApiResponse.setValue(apiResponse)
        );
        return mApiResponse;
    }
}
