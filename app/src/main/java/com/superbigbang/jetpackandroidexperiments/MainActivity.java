package com.superbigbang.jetpackandroidexperiments;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.superbigbang.jetpackandroidexperiments.databinding.ActivityMainBinding;
import com.superbigbang.jetpackandroidexperiments.model.CardItem;
import com.superbigbang.jetpackandroidexperiments.model.HeaderItem;
import com.superbigbang.jetpackandroidexperiments.model.response.Issue;
import com.superbigbang.jetpackandroidexperiments.viewModels.MainActivityViewModel;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_main;

    Section fromResponseIssuesLoadingSection;
    private MainActivityViewModel mViewModel;
    private ActivityMainBinding binding;
    private GroupAdapter groupAdapter;
    private GridLayoutManager layoutManager;

    private CardItem.OnCardItemChildClickListener onCardItemChildClickListener = (item, view) -> {
        switch (view.getId()) {
            case R.id.author_avatar:
                Toast.makeText(MainActivity.this, item.getAuthor_avatar(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.creatorName:
                Toast.makeText(MainActivity.this, item.getCreatorName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.titleOfIssue:
                Toast.makeText(MainActivity.this, "Click on :" + item.getTitleOfIssue(), Toast.LENGTH_LONG).show();
                break;
        }
    };

    void handleError(Throwable error) {
        Timber.e(error);
        Toast.makeText(this, "Get ERROR, show info in Logcat", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        // Handle changes emitted by LiveData
        mViewModel.getApiResponse().observe(this, apiResponse -> {
            if (apiResponse.getError() != null) {
                handleError(apiResponse.getError());
            } else {
                handleResponse(apiResponse.getIssues());
            }
        });

        groupAdapter = mViewModel.getGroupAdapter().getValue();

        if (groupAdapter != null) {
            layoutManager = new GridLayoutManager(this, groupAdapter.getSpanCount());
            layoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());
        }

        final RecyclerView recyclerView = binding.listOfIssueses;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        Timber.plant(new Timber.DebugTree()); //next time move it in Application class
    }

    private void populateAdapter(List issues) {
        fromResponseIssuesLoadingSection = new Section(new HeaderItem(R.string.issues_of_this_repository));
        for (int i = 0; i < issues.size(); i++) {
            CardItem cardItem = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cardItem = new CardItem(getResources().getColor(R.color.blue_200, getTheme()),
                        ((Issue) issues.get(i)).getTitle(),
                        ((Issue) issues.get(i)).getUser().getLogin(),
                        ((Issue) issues.get(i)).getUser().getAvatarUrl(),
                        onCardItemChildClickListener);
            } else {
                //Deprecated for versions API =>23
                cardItem = new CardItem(getResources().getColor(R.color.blue_200),
                        ((Issue) issues.get(i)).getTitle(),
                        ((Issue) issues.get(i)).getUser().getLogin(),
                        ((Issue) issues.get(i)).getUser().getAvatarUrl(),
                        onCardItemChildClickListener);
            }

            // cardItem.setOnItemClickListener();
            fromResponseIssuesLoadingSection.add(cardItem);
        }
        groupAdapter.add(fromResponseIssuesLoadingSection);
    }

    void handleResponse(List issues) {
        //operate
        if (issues != null && issues.get(0) != null) {
            Toast.makeText(this, "Succeful get list of issueses", Toast.LENGTH_LONG).show();
            groupAdapter.clear();
            populateAdapter(issues);
        } else {
            Toast.makeText(this, "No data found for this query", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.button)
    public void onButtonClicked() {
        if (binding.ownerName.getText().length() == 0 || binding.repositoryName.getText().length() == 0) {
            Toast.makeText(this, "Please enter Owner name and Repository", Toast.LENGTH_LONG).show();
        } else
            mViewModel.loadIssues(binding.ownerName.getText().toString().trim(), binding.repositoryName.getText().toString().trim());
    }

    /*   private OnItemClickListener onItemClickListener = (item, view) -> {
        if (item instanceof CardItem) {
            CardItem cardItem = (CardItem) item;
            if (!TextUtils.isEmpty(cardItem.)) {
                Toast.makeText(MainActivity.this, cardItem.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    };*/

    /* private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(Item item, View view) {
            if (item instanceof CardItem) {
                CardItem cardItem = (CardItem) item;
                if (!TextUtils.isEmpty(cardItem.getText())) {
                    Toast.makeText(MainActivity.this, "Long clicked: " + cardItem.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        }
    };
            return false;
        }
    };*/
}
