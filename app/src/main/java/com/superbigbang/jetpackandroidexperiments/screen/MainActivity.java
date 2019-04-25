package com.superbigbang.jetpackandroidexperiments.screen;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.ActivityMainBinding;
import com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems.SwipeTouchCallback;
import com.superbigbang.jetpackandroidexperiments.screen.viewModels.MainActivityViewModel;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.TouchCallback;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_main;

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding binding;
    private GroupAdapter mGroupAdapter;
    private GridLayoutManager layoutManager;

    private TouchCallback touchCallback = new SwipeTouchCallback(R.color.transparent) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mViewModel.removeSwappedItem(viewHolder.getAdapterPosition());
        }
    };

    @OnClick(R.id.showIssuesbutton)
    public void onButtonClicked() {
        if (binding.ownerName.getText().length() == 0 || binding.repositoryName.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.enter_Owner_name_and_Repository), Toast.LENGTH_LONG).show();
        } else
            mViewModel.loadIssues(binding.ownerName.getText().toString().trim(), binding.repositoryName.getText().toString().trim());
    }

    @OnClick(R.id.showFavoriteIssuesButton)
    public void onShowFaworiteButtonClicked() {
        mViewModel.populateFavoriteAdapter();
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
                mViewModel.handleError(apiResponse.getError(), MainActivity.this.getString(R.string.error_in_retrieving_data));
            } else {
                mViewModel.handleResponse(apiResponse.getIssues());
            }
        });

        mGroupAdapter = mViewModel.getGroupAdapter().getValue();

        if (mGroupAdapter != null) {
            layoutManager = new GridLayoutManager(this, mGroupAdapter.getSpanCount());
            layoutManager.setSpanSizeLookup(mGroupAdapter.getSpanSizeLookup());
        }

        final RecyclerView recyclerView = binding.listOfIssueses;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mGroupAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /*  ====================== Code that may be needed in the future ===============================
    private OnItemClickListener onItemClickListener = (item, view) -> {
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
