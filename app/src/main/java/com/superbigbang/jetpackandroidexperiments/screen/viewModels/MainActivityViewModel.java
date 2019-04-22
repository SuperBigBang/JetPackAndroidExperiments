package com.superbigbang.jetpackandroidexperiments.screen.viewModels;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.superbigbang.jetpackandroidexperiments.ExtendApplication;
import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.di.modules.repositoryModule.IssueRepository;
import com.superbigbang.jetpackandroidexperiments.model.issueResponse.ApiResponse;
import com.superbigbang.jetpackandroidexperiments.model.issueResponse.Issue;
import com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems.CardItem;
import com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems.HeaderItem;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class MainActivityViewModel extends ViewModel {

    private MediatorLiveData<ApiResponse> mApiResponse;
    private MutableLiveData<GroupAdapter> mGroupAdapter;

    private IssueRepository mIssueRepository;
    private Section fromResponseIssuesLoadingSection;
    private Context mApplicationContext;
    private Resources mResourcesFromAppContext;
    private CardItem.OnCardItemChildClickListener onCardItemChildClickListener = (item, view) -> {
        switch (view.getId()) {
            case R.id.author_avatar:
                Toast.makeText(mApplicationContext, item.getAuthor_avatar(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.creatorName:
                Toast.makeText(mApplicationContext, item.getCreatorName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.titleOfIssue:
                Toast.makeText(mApplicationContext, "Click on :" + item.getTitleOfIssue(), Toast.LENGTH_LONG).show();
                break;
        }
    };

    // No argument constructor
    public MainActivityViewModel() {
        mApiResponse = new MediatorLiveData<>();
        mIssueRepository = ExtendApplication.getBaseComponent().getIssueRepositoryImpl();
        mGroupAdapter = new MutableLiveData<>();
        mGroupAdapter.setValue(setupGroupAdapter());
        mApplicationContext = ExtendApplication.getBaseComponent().getContext();
        mResourcesFromAppContext = ExtendApplication.getBaseComponent().getContext().getResources();


    }

    public void handleResponse(List issues) {
        //operate
        if (issues != null && issues.get(0) != null) {
            Toast.makeText(mApplicationContext, R.string.succeful_get_list_of_issueses, Toast.LENGTH_LONG).show();
            Objects.requireNonNull(mGroupAdapter.getValue()).clear();
            populateAdapter(issues);
        } else {
            Toast.makeText(mApplicationContext, R.string.no_data_found_for_this_query, Toast.LENGTH_LONG).show();
        }
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

    public void handleError(Throwable error) {
        Timber.e(error);
        Toast.makeText(mApplicationContext, R.string.get_ERROR_show_info_in_Logcat, Toast.LENGTH_LONG).show();
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

    private void populateAdapter(List issues) {
        fromResponseIssuesLoadingSection = new Section(new HeaderItem(R.string.issues_of_this_repository));
        for (int i = 0; i < issues.size(); i++) {
            CardItem cardItem = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cardItem = new CardItem(mResourcesFromAppContext.getColor(R.color.blue_200, mApplicationContext.getTheme()),
                        ((Issue) issues.get(i)).getTitle(),
                        ((Issue) issues.get(i)).getUser().getLogin(),
                        ((Issue) issues.get(i)).getUser().getAvatarUrl(),
                        onCardItemChildClickListener);
            } else {
                cardItem = new CardItem(mResourcesFromAppContext.getColor(R.color.blue_200),
                        ((Issue) issues.get(i)).getTitle(),
                        ((Issue) issues.get(i)).getUser().getLogin(),
                        ((Issue) issues.get(i)).getUser().getAvatarUrl(),
                        onCardItemChildClickListener);
            }

            // cardItem.setOnItemClickListener();
            fromResponseIssuesLoadingSection.add(cardItem);
        }
        Objects.requireNonNull(mGroupAdapter.getValue()).add(fromResponseIssuesLoadingSection);
    }
}
