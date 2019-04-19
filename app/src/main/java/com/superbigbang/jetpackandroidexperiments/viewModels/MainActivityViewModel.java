package com.superbigbang.jetpackandroidexperiments.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;
import com.superbigbang.jetpackandroidexperiments.repository.IssueRepository;
import com.superbigbang.jetpackandroidexperiments.repository.IssueRepositoryImpl;
import com.xwray.groupie.GroupAdapter;

public class MainActivityViewModel extends ViewModel {

    private MediatorLiveData<ApiResponse> mApiResponse;
    private IssueRepository mIssueRepository;
    private MutableLiveData<GroupAdapter> mGroupAdapter;

    // No argument constructor
    public MainActivityViewModel() {
        mApiResponse = new MediatorLiveData<>();
        mIssueRepository = new IssueRepositoryImpl();
        mGroupAdapter = new MutableLiveData<>();
        mGroupAdapter.setValue(setupGroupAdapter());
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
