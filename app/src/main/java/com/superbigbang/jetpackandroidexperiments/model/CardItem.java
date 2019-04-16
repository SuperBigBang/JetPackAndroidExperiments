package com.superbigbang.jetpackandroidexperiments.model;

import android.view.View;

import com.squareup.picasso.Picasso;
import com.superbigbang.jetpackandroidexperiments.R;
import com.superbigbang.jetpackandroidexperiments.databinding.IssueItemBinding;
import com.xwray.groupie.databinding.BindableItem;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardItem extends BindableItem<IssueItemBinding> {

    //   private OnItemClickListener onItemClickListener;
    @ColorInt
    private int colorRes;
    private CharSequence titleOfIssue;
    private CharSequence creatorName;
    private CharSequence author_avatar;
    private OnCardItemChildClickListener onCardItemChildClickListener;

    public CardItem(@ColorInt int colorRes, OnCardItemChildClickListener onCardItemChildClickListener) {
        this(colorRes, "", "", "", onCardItemChildClickListener);
    }

    public CardItem(@ColorInt int colorRes, CharSequence titleOfIssue, CharSequence creatorName, CharSequence author_avatar, OnCardItemChildClickListener onCardItemChildClickListener) {
        this.colorRes = colorRes;
        this.titleOfIssue = titleOfIssue;
        this.creatorName = creatorName;
        this.author_avatar = author_avatar;
        this.onCardItemChildClickListener = onCardItemChildClickListener;
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
    }

    public interface OnCardItemChildClickListener {
        void OnChildClick(CardItem cardItem, View view);
    }
}
