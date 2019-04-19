package com.superbigbang.jetpackandroidexperiments.screen.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.superbigbang.jetpackandroidexperiments.ExtendApplication;
import com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.IssueRepository;
import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;
import com.xwray.groupie.GroupAdapter;

public class MainActivityViewModel extends ViewModel {

    private MediatorLiveData<ApiResponse> mApiResponse;
    private MutableLiveData<GroupAdapter> mGroupAdapter;

    private IssueRepository mIssueRepository;

    // No argument constructor
    public MainActivityViewModel() {
        mApiResponse = new MediatorLiveData<>();
        mIssueRepository = ExtendApplication.getBaseComponent().getIssueRepositoryImpl();
        mGroupAdapter = new MutableLiveData<>();
        mGroupAdapter.setValue(setupGroupAdapter());
    }

    @NonNull
    public LiveData<ApiResponse> getApiResponse() {
        return mApiResponse;
    }

    public void loadIssues(@NonNull String user, @NonNull String repo) {
        mApiResponse.addSource(
                mIssueRepository.getIssues(user, repo),
                apiResponse -> mApiResponse.setValue(apiResponse)
        );
    }

    @NonNull
    public LiveData<GroupAdapter> getGroupAdapter() {
        return mGroupAdapter;
    }

    private GroupAdapter setupGroupAdapter() {
        GroupAdapter groupAdapter = new GroupAdapter();
        groupAdapter.setSpanCount(12);
        // groupAdapter.setOnItemClickListener(onItemClickListener);
        // groupAdapter.setOnItemLongClickListener(onItemLongClickListener);
        return groupAdapter;
    }
}
