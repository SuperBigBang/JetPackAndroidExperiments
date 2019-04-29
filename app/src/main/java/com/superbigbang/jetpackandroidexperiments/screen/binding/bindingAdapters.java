package com.superbigbang.jetpackandroidexperiments.screen.binding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class bindingAdapters {
    @BindingAdapter({"android:url", "android:errorImage"})
    public static void loadImage(ImageView view, String url, Drawable errorImage) {
        Picasso.get().load(url).error(errorImage).into(view);
    }
}
