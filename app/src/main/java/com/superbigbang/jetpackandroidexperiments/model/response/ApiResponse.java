package com.superbigbang.jetpackandroidexperiments.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private List<Issue> issues;
    private Throwable error;

    public ApiResponse(List<Issue> issues) {
        this.issues = issues;
        this.error = null;
    }

    public ApiResponse(Throwable error) {
        this.error = error;
        this.issues = null;
    }
}
