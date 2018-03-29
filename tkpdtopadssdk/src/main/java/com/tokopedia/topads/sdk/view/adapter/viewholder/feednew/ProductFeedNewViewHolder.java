package com.tokopedia.topads.sdk.view.adapter.viewholder.feednew;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ProductFeedNewViewModel;

/**
 * @author by milhamj on 29/03/18.
 */

public class ProductFeedNewViewHolder extends AbstractViewHolder<ProductFeedNewViewModel>
        implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_feed;

    private Context context;
    private ImageLoader imageLoader;
    private LocalAdsClickListener itemClickListener;

    public ProductFeedNewViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener
            itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.context = itemView.getContext();
        this.imageLoader = imageLoader;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bind(ProductFeedNewViewModel element) {

    }
}
