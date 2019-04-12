package com.superbigbang.jetpackandroidexperiments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.superbigbang.jetpackandroidexperiments.model.ListIssuesViewModel;
import com.superbigbang.jetpackandroidexperiments.model.response.Issue;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ownerName)
    EditText ownerName;
    @BindView(R.id.repositoryName)
    EditText repositoryName;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.listOfIssueses)
    RecyclerView listOfIssueses;
    private ListIssuesViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree()); //next time move it in Application class
        ButterKnife.bind(this);
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
        if (issues != null && issues.get(0) != null) {
            Issue issue = (Issue) issues.get(0);
            Timber.e(issue.getTitle());
            Timber.e(issue.getId().toString());
            Timber.e(issue.getUser().getLogin());
            Toast.makeText(this, "Succeful get list of issueses", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No data found for this query", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.button)
    public void onButtonClicked() {
        if (ownerName.getText().length() == 0 || repositoryName.getText().length() == 0) {
            Toast.makeText(this, "Please enter Owner name and Repository", Toast.LENGTH_LONG).show();
        } else
            mViewModel.loadIssues(ownerName.getText().toString(), repositoryName.getText().toString());
    }
}
