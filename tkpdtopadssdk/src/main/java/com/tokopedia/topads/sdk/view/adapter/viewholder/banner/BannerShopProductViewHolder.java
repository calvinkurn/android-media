package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewHolder extends AbstractViewHolder<BannerShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_banner_shop;
    private static final String TAG = BannerShopProductViewHolder.class.getSimpleName();


    public BannerShopProductViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(BannerShopProductViewModel element) {

    }
}
