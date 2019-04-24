package com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems;

import android.graphics.drawable.Animatable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.IssueItemBinding;
import com.xwray.groupie.databinding.BindableItem;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

@Getter
@Setter
public class CardItem extends BindableItem<IssueItemBinding> {

    public static final String FAVORITE = "FAVORITE";

    //   private OnItemClickListener onItemClickListener;
    @ColorInt
    private int colorRes;
    private CharSequence titleOfIssue;
    private CharSequence creatorName;
    private CharSequence author_avatar;
    private OnCardItemChildClickListener onCardItemChildClickListener;

    private OnFavoriteListener onFavoriteListener;
    private boolean checked = false;
    private boolean inProgress = false;


    public CardItem(@ColorInt int colorRes, OnCardItemChildClickListener onCardItemChildClickListener, OnFavoriteListener onFavoriteListener) {
        this(colorRes, "", "", "", onCardItemChildClickListener, onFavoriteListener);
    }

    public CardItem(@ColorInt int colorRes, CharSequence titleOfIssue, CharSequence creatorName, CharSequence author_avatar, OnCardItemChildClickListener onCardItemChildClickListener, OnFavoriteListener onFavoriteListener) {
        this.colorRes = colorRes;
        this.titleOfIssue = titleOfIssue;
        this.creatorName = creatorName;
        this.author_avatar = author_avatar;
        this.onCardItemChildClickListener = onCardItemChildClickListener;
        this.onFavoriteListener = onFavoriteListener;
        //   getExtras().put(INSET_TYPE_KEY, INSET);
    }

    @Override
    public int getLayout() {
        return R.layout.issue_item;
    }

    @Override
    public void bind(@NonNull IssueItemBinding viewBinding, int position) {
        //viewBinding.getRoot().setBackgroundColor(colorRes);
        viewBinding.titleOfIssue.setText(titleOfIssue);
        viewBinding.creatorName.setText(creatorName);
        Picasso.get().load(author_avatar.toString()).into(viewBinding.authorAvatar);
        viewBinding.titleOfIssue.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(CardItem.this, v));
        viewBinding.creatorName.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(CardItem.this, v));
        viewBinding.authorAvatar.setOnClickListener(v -> onCardItemChildClickListener.OnChildClick(CardItem.this, v));

        bindHeart(viewBinding);
        viewBinding.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inProgress = true;
                animateProgress(viewBinding);

                onFavoriteListener.onFavorite(CardItem.this, !checked);
            }
        });
    }

    private void bindHeart(IssueItemBinding binding) {
        if (inProgress) {
            animateProgress(binding);
        } else {
            binding.favorite.setImageResource(R.drawable.favorite_state_list);
        }
        binding.favorite.setChecked(checked);
    }

    private void animateProgress(IssueItemBinding binding) {
        binding.favorite.setImageResource(R.drawable.avd_favorite_progress);
        ((Animatable) binding.favorite.getDrawable()).start();
    }

    public void setFavorite(boolean favorite) {
        inProgress = false;
        checked = favorite;
    }

    public interface OnCardItemChildClickListener {
        void OnChildClick(CardItem cardItem, View view);
    }

    public interface OnFavoriteListener {
        void onFavorite(CardItem item, boolean favorite);
    }

    public void setEndStateForFavoriteMarker(final CardItem item, final boolean favorite) {
        Completable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    // Network request was successful!
                    Timber.e("Current Thread: %s", Thread.currentThread().getName());
                    item.setFavorite(favorite);
                    item.notifyChanged(CardItem.FAVORITE);
                })
                .subscribe();
    }
}
