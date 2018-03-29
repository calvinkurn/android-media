package com.tokopedia.topads.sdk.view.adapter.viewholder.feednew;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ShopFeedNewViewModel;

/**
 * @author by milhamj on 29/03/18.
 */

public class ShopFeedNewViewHolder extends AbstractViewHolder<ShopFeedNewViewModel>
        implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_shop_feed_plus;

    private Context context;
    private ImageLoader imageLoader;
    private LocalAdsClickListener itemClickListener;

    public ShopFeedNewViewHolder(View itemView, ImageLoader imageLoader,
                                 LocalAdsClickListener itemClickListener) {
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
    public void bind(ShopFeedNewViewModel element) {

    }
}
