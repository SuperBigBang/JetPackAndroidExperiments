package com.superbigbang.jetpackandroidexperiments.screen;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.ActivityMainBinding;
import com.superbigbang.jetpackandroidexperiments.screen.viewModels.MainActivityViewModel;
import com.xwray.groupie.GroupAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_main;

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding binding;
    private GroupAdapter groupAdapter;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, LAYOUT);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        // Handle changes emitted by LiveData
        mViewModel.getApiResponse().observe(this, apiResponse -> {
            if (apiResponse.getError() != null) {
                mViewModel.handleError(apiResponse.getError(), getString(R.string.error_in_retrieving_data));
            } else {
                mViewModel.handleResponse(apiResponse.getIssues());
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
    }

    @OnClick(R.id.button)
    public void onButtonClicked() {
        if (binding.ownerName.getText().length() == 0 || binding.repositoryName.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.enter_Owner_name_and_Repository), Toast.LENGTH_LONG).show();
        } else
            mViewModel.loadIssues(binding.ownerName.getText().toString().trim(), binding.repositoryName.getText().toString().trim());
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
