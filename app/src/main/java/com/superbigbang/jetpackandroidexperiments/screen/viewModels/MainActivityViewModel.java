package com.superbigbang.jetpackandroidexperiments.screen.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;
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
import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.AppDatabase;
import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao.SavedIssueCard;
import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao.SavedIssueCardDao;
import com.superbigbang.jetpackandroidexperiments.model.issueResponse.ApiResponse;
import com.superbigbang.jetpackandroidexperiments.model.issueResponse.Issue;
import com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems.CardItem;
import com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems.HeaderItem;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivityViewModel extends ViewModel {

    private MediatorLiveData<ApiResponse> mApiResponse;
    private MutableLiveData<GroupAdapter> mGroupAdapter;

    private AppDatabase mDatabase;
    private SavedIssueCardDao mSavedIssueCardDao;
    private IssueRepository mIssueRepository;
    private Section mFromResponseIssuesLoadingSection;
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
    private CardItem.OnFavoriteListener onFavoriteListener = new CardItem.OnFavoriteListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onFavorite(final CardItem item, final boolean favorite) {
            // Pretend to make a network request
            Observable.fromCallable(new CallableAddNewIssueToDB(item) {
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            /////////////The code is not cleaned in lambda for clarity
                            new Consumer<String>() {
                                @Override
                                public void accept(final String string) throws Exception {
                                    showCurrentThread();
                                    Toast.makeText(mApplicationContext, string + " - issue successfully added to DB", Toast.LENGTH_LONG).show();
                                    item.setEndStateForFavoriteMarker(item, favorite);
                                }
                            },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(final Throwable throwable) throws Exception {
                                    if (throwable instanceof SQLiteConstraintException) {
                                        handleError(throwable, mApplicationContext.getString(R.string.error_card_already_in_DB));
                                    } else {
                                        handleError(throwable, null);
                                    }
                                    item.setEndStateForFavoriteMarker(item, !favorite);
                                }
                            });
        }
    };

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

    // No argument constructor
    public MainActivityViewModel() {
        mApiResponse = new MediatorLiveData<>();
        mIssueRepository = ExtendApplication.getBaseComponent().getIssueRepositoryImpl();
        mGroupAdapter = new MutableLiveData<>();
        mGroupAdapter.setValue(setupGroupAdapter());
        mApplicationContext = ExtendApplication.getBaseComponent().getContext();
        mResourcesFromAppContext = ExtendApplication.getBaseComponent().getContext().getResources();
        mDatabase = ExtendApplication.getBaseComponent().getAppDatabase();
        mSavedIssueCardDao = mDatabase.savedIssueCardDao();
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

    public void handleError(Throwable error, String extraInfo) {
        Timber.e(error);
        if (extraInfo == null) {
            Toast.makeText(mApplicationContext, R.string.get_ERROR_show_info_in_Logcat, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mApplicationContext, extraInfo, Toast.LENGTH_LONG).show();
        }
    }

    private void populateAdapter(List issues) {
        mFromResponseIssuesLoadingSection = new Section(new HeaderItem(R.string.issues_of_this_repository));
        for (int i = 0; i < issues.size(); i++) {
            CardItem cardItem = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cardItem = new CardItem(mResourcesFromAppContext.getColor(R.color.blue_200, mApplicationContext.getTheme()),
                        ((Issue) issues.get(i)).getTitle(),
                        ((Issue) issues.get(i)).getUser().getLogin(),
                        ((Issue) issues.get(i)).getUser().getAvatarUrl(),
                        onCardItemChildClickListener,
                        onFavoriteListener);
            } else {
                cardItem = new CardItem(mResourcesFromAppContext.getColor(R.color.blue_200),
                        ((Issue) issues.get(i)).getTitle(),
                        ((Issue) issues.get(i)).getUser().getLogin(),
                        ((Issue) issues.get(i)).getUser().getAvatarUrl(),
                        onCardItemChildClickListener,
                        onFavoriteListener);
            }

            // cardItem.setOnItemClickListener();
            mFromResponseIssuesLoadingSection.add(cardItem);
        }
        Objects.requireNonNull(mGroupAdapter.getValue()).add(mFromResponseIssuesLoadingSection);
    }

    ///////////////////////Database operations///////////////////////////

    /////insert method and his wrapper:
    private String addNewIssueToDB(final CardItem item) throws Exception {
        SavedIssueCard savedIssueCard = new SavedIssueCard();
        savedIssueCard.setId(item.getId());
        savedIssueCard.setAuthor_avatar(item.getAuthor_avatar().toString());
        savedIssueCard.setCreatorName(item.getCreatorName().toString());
        savedIssueCard.setTitleOfIssue(item.getTitleOfIssue().toString());
        mSavedIssueCardDao.insert(savedIssueCard);
        return savedIssueCard.getTitleOfIssue();
    }

    ///////////////////////Testing methods///////////////////////////
    private void showCurrentThread() {
        Timber.e("Current Thread: %s", Thread.currentThread().getName());
    }

    /////

    private class CallableAddNewIssueToDB implements Callable<String> {
        private final CardItem cardItem;

        private CallableAddNewIssueToDB(final CardItem carditem) {
            this.cardItem = carditem;
        }

        @Override
        public String call() throws Exception {
            showCurrentThread();
            return addNewIssueToDB(cardItem);
        }
    }
}
