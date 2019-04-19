package com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.retrofit;

import com.superbigbang.jetpackandroidexperiments.model.response.Issue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApiService {
    @GET("/repos/{owner}/{repo}/issues")
    Call<List<Issue>> getIssues(@Path("owner") String owner, @Path("repo") String repo);
}
