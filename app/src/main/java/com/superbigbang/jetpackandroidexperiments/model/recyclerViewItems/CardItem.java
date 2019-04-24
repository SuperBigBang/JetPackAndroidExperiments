package com.superbigbang.jetpackandroidexperiments.model.recyclerViewItems;

import android.annotation.SuppressLint;
import android.graphics.drawable.Animatable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.superbigbang.jetpackandroidexperiments.ExtendApplication;
import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.IssueItemBinding;
import com.xwray.groupie.databinding.BindableItem;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
    private long id;
    private CharSequence titleOfIssue;
    private CharSequence creatorName;
    private CharSequence author_avatar;
    private OnCardItemChildClickListener onCardItemChildClickListener;

    private OnFavoriteListener onFavoriteListener;
    private boolean checked = false;
    private boolean inProgress = false;

    public CardItem(@ColorInt int colorRes, long id, CharSequence titleOfIssue, CharSequence creatorName, CharSequence author_avatar, OnCardItemChildClickListener onCardItemChildClickListener, OnFavoriteListener onFavoriteListener) {
        this.colorRes = colorRes;
        this.titleOfIssue = titleOfIssue;
        this.creatorName = creatorName;
        this.author_avatar = author_avatar;
        this.onCardItemChildClickListener = onCardItemChildClickListener;
        this.onFavoriteListener = onFavoriteListener;
        this.id = id;
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

    @SuppressLint("CheckResult")
    private void bindHeart(IssueItemBinding binding) {
        if (inProgress) {
            animateProgress(binding);
        } else {
            binding.favorite.setImageResource(R.drawable.favorite_state_list);
        }

        Observable.fromCallable(new CallablefindIssueByIdInDB(getId()) {
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        /////////////The code is not cleaned in lambda for clarity
                        new Consumer<Boolean>() {
                            @Override
                            public void accept(final Boolean cardAlreadyOnDB) throws Exception {
                                showCurrentThread();
                                setChecked(cardAlreadyOnDB);
                                binding.favorite.setChecked(cardAlreadyOnDB);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {
                                handleError(throwable, null);
                                setChecked(false);
                                binding.favorite.setChecked(false);
                            }
                        });
    }

    private void handleError(Throwable error, String extraInfo) {
        Timber.e(error);
        if (extraInfo == null) {
            Toast.makeText(ExtendApplication.getBaseComponent().getContext(), R.string.get_ERROR_show_info_in_Logcat, Toast.LENGTH_LONG).show();
        }
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

    ///////////////////////Testing methods///////////////////////////
    private void showCurrentThread() {
        Timber.e("Current Thread on find issue by id: %s", Thread.currentThread().getName());
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
