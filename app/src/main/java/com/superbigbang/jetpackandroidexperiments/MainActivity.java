package com.superbigbang.jetpackandroidexperiments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.superbigbang.jetpackandroidexperiments.databinding.ActivityMainBinding;
import com.superbigbang.jetpackandroidexperiments.model.CardItem;
import com.superbigbang.jetpackandroidexperiments.model.HeaderItem;
import com.superbigbang.jetpackandroidexperiments.model.ListIssuesViewModel;
import com.superbigbang.jetpackandroidexperiments.model.response.Issue;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.OnItemLongClickListener;
import com.xwray.groupie.Section;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    Section infiniteLoadingSection;
    private ListIssuesViewModel mViewModel;
    /* @BindView(R.id.ownerName)
     EditText ownerName;
     @BindView(R.id.repositoryName)
     EditText repositoryName;
     @BindView(R.id.button)
     Button button;
     @BindView(R.id.listOfIssueses)
     RecyclerView listOfIssueses;*/
    private ActivityMainBinding binding;
    private GroupAdapter groupAdapter;
    private GridLayoutManager layoutManager;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(Item item, View view) {
           /* if (item instanceof CardItem) {
                CardItem cardItem = (CardItem) item;
                if (!TextUtils.isEmpty(cardItem.getText())) {
                    Toast.makeText(MainActivity.this, cardItem.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        }
    };
    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(Item item, View view) {
      /*      if (item instanceof CardItem) {
                CardItem cardItem = (CardItem) item;
                if (!TextUtils.isEmpty(cardItem.getText())) {
                    Toast.makeText(MainActivity.this, "Long clicked: " + cardItem.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        }
    };*/
            return false;
        }
    };

    void handleError(Throwable error) {
        Timber.e(error);
        Toast.makeText(this, "Get ERROR, show info in Logcat", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);

        groupAdapter = new GroupAdapter();
        groupAdapter.setOnItemClickListener(onItemClickListener);
        groupAdapter.setOnItemLongClickListener(onItemLongClickListener);
        groupAdapter.setSpanCount(12);

        layoutManager = new GridLayoutManager(this, groupAdapter.getSpanCount());
        layoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());

        final RecyclerView recyclerView = binding.listOfIssueses;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

     /*  recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager) {
            @Override public void onLoadMore(int currentPage) {
                for (int i = 0; i < 5; i++) {
                    infiniteLoadingSection.add(new CardItem(getResources().getColor(R.color.blue_200)));
                }
            }
        });*/

        Timber.plant(new Timber.DebugTree()); //next time move it in Application class

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

    private void populateAdapter(List issues) {
        // Infinite loading section
        infiniteLoadingSection = new Section(new HeaderItem(R.string.issues_of_this_repository));
        for (int i = 0; i < issues.size(); i++) {
            infiniteLoadingSection.add(new CardItem(getResources().getColor(R.color.blue_200),
                    ((Issue) issues.get(i)).getTitle(),
                    ((Issue) issues.get(i)).getUser().getLogin(),
                    ((Issue) issues.get(i)).getUser().getAvatarUrl()));
        }
        groupAdapter.add(infiniteLoadingSection);
    }

    void handleResponse(List issues) {
        //operate
        if (issues != null && issues.get(0) != null) {
            Issue issue = (Issue) issues.get(0);
            Timber.e(issue.getTitle());
            Timber.e(issue.getId().toString());
            Timber.e(issue.getUser().getLogin());
            Toast.makeText(this, "Succeful get list of issueses", Toast.LENGTH_LONG).show();
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
            mViewModel.loadIssues(binding.ownerName.getText().toString(), binding.repositoryName.getText().toString());
    }
}
