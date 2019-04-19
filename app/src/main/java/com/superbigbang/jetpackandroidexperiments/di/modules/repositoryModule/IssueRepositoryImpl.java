package com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.retrofit.GithubApiService;
import com.superbigbang.jetpackandroidexperiments.model.response.ApiResponse;
import com.superbigbang.jetpackandroidexperiments.model.response.Issue;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class IssueRepositoryImpl implements IssueRepository {

    private static final String BASE_URL = "https://api.github.com/";
    private GithubApiService mApiService;

    public IssueRepositoryImpl() {
        mApiService = builderRetrofit(BASE_URL).create(GithubApiService.class);
    }

    public void changeServiceBaseURL(String BASE_URL) {
        mApiService = builderRetrofit(BASE_URL).create(GithubApiService.class);
    }

    private Retrofit builderRetrofit(String BASE_URL) {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
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
