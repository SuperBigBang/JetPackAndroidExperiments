package com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.FavoriteIssueItemBinding;
import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao.SavedIssueCard;
import com.xwray.groupie.databinding.BindableItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteCardItem extends BindableItem<FavoriteIssueItemBinding> {

    private SavedIssueCard savedIssueCard;
    private OnCardItemChildClickListener onCardItemChildClickListener;

    public FavoriteCardItem(SavedIssueCard savedIssueCard, OnCardItemChildClickListener onCardItemChildClickListener) {
        this.savedIssueCard = savedIssueCard;
        this.onCardItemChildClickListener = onCardItemChildClickListener;
    }

    @Override
    public int getLayout() {
        return R.layout.favorite_issue_item;
    }

    @Override
    public void bind(@NonNull FavoriteIssueItemBinding viewBinding, int position) {
        viewBinding.setSavedIssueCard(savedIssueCard);
        viewBinding.titleOfSavedIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(FavoriteCardItem.this, v));
        viewBinding.creatorNameOfSavedIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(FavoriteCardItem.this, v));
        viewBinding.authorAvatarOfSavedIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(FavoriteCardItem.this, v));
        viewBinding.executePendingBindings();
    }

    @Override
    public int getSwipeDirs() {
        return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    }

    public interface OnCardItemChildClickListener {
        void OnChildClick(FavoriteCardItem cardItem, View view);
    }
}
