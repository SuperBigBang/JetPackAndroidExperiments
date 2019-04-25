package com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.squareup.picasso.Picasso;
import com.superbigbang.jetpackandroidexperiments.ExtendApplication;
import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.FavoriteIssueItemBinding;
import com.xwray.groupie.databinding.BindableItem;

import java.util.concurrent.Callable;

import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

@Getter
@Setter
public class FavoriteCardItem extends BindableItem<FavoriteIssueItemBinding> {

    @ColorInt
    private int colorRes;
    private long id;
    private CharSequence titleOfIssue;
    private CharSequence creatorName;
    private CharSequence author_avatar;
    private OnCardItemChildClickListener onCardItemChildClickListener;

    public FavoriteCardItem(@ColorInt int colorRes, long id, CharSequence titleOfIssue, CharSequence creatorName, CharSequence author_avatar, OnCardItemChildClickListener onCardItemChildClickListener) {
        this.colorRes = colorRes;
        this.titleOfIssue = titleOfIssue;
        this.creatorName = creatorName;
        this.author_avatar = author_avatar;
        this.onCardItemChildClickListener = onCardItemChildClickListener;
        this.id = id;
    }

    @Override
    public int getLayout() {
        return R.layout.favorite_issue_item;
    }

    @Override
    public void bind(@NonNull FavoriteIssueItemBinding viewBinding, int position) {
        //viewBinding.getRoot().setBackgroundColor(colorRes);
        viewBinding.titleOfSavedIssue.setText(titleOfIssue);
        viewBinding.creatorNameOfSavedIssue.setText(creatorName);
        Picasso.get().load(author_avatar.toString()).into(viewBinding.authorAvatarOfSavedIssue);
        viewBinding.titleOfSavedIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(FavoriteCardItem.this, v));
        viewBinding.creatorNameOfSavedIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(FavoriteCardItem.this, v));
        viewBinding.authorAvatarOfSavedIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(FavoriteCardItem.this, v));
    }

    @Override
    public int getSwipeDirs() {
        return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    }

    ///////////////////////Testing methods///////////////////////////
    private void showCurrentThread() {
        Timber.e("Current Thread on find issue by id: %s", Thread.currentThread().getName());
    }

    public interface OnCardItemChildClickListener {
        void OnChildClick(FavoriteCardItem cardItem, View view);
    }

    ///////////////////////Database operations///////////////////////////
    private class CallablefindIssueByIdInDB implements Callable<Boolean> {
        private final long id;

        private CallablefindIssueByIdInDB(final long id) {
            this.id = id;
        }

        @Override
        public Boolean call() throws Exception {
            showCurrentThread();
            return ExtendApplication.getBaseComponent().getAppDatabase().savedIssueCardDao().getById(id) != null;
        }
    }
}
