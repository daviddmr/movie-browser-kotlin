package com.arctouch.codechallenge.util;

import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import com.arctouch.codechallenge.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class CustomBindingAdapter {
    @BindingAdapter({"bind:image_url"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(imageView);
    }
}
