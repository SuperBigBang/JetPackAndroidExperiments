package com.superbigbang.jetpackandroidexperiments;

import android.os.Bundle;
import android.widget.Toast;

import com.superbigbang.jetpackandroidexperiments.model.ListIssuesViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private ListIssuesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(ListIssuesViewModel.class);
        //setupView();
        // Handle changes emitted by LiveData
        mViewModel.getApiResponse().observe(this, apiResponse -> {
            if (apiResponse.getError() != null) {
                handleError(apiResponse.getError());
            } else {
                handleResponse(apiResponse.getIssues());
            }
        });
    }

    void handleError(Throwable error) {
        Timber.e(error);
        Toast.makeText(this, "Get ERROR, show info in Logcat", Toast.LENGTH_LONG).show();
    }

    void handleResponse(List issues) {
        //operate
        Toast.makeText(this, "Succeful get list of issueses", Toast.LENGTH_LONG).show();
    }
}
