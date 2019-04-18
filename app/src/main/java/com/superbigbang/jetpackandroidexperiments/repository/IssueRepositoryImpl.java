package com.superbigbang.jetpackandroidexperiments.repository;

import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;
import com.superbigbang.jetpackandroidexperiments.model.response.Issue;
import com.superbigbang.jetpackandroidexperiments.retrofit.GithubApiService;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueRepositoryImpl implements IssueRepository {

    public static final String BASE_URL = "https://api.github.com/";
    private GithubApiService mApiService;

    public IssueRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        mApiService = retrofit.create(GithubApiService.class);
    }

    public LiveData<ApiResponse> getIssues(String owner, String repo) {
        final MutableLiveData<ApiResponse> liveData = new MutableLiveData<>();
        Call<List<Issue>> call = mApiService.getIssues(owner, repo);
        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                liveData.postValue(new ApiResponse(response.body()));
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                liveData.postValue(new ApiResponse(t));
            }
        });
        return liveData;
    }
}
