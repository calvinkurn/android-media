package com.tokopedia.topads.sdk.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductGridViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerAdsAdapterTypeFactory implements BannerAdsTypeFactory {

    public BannerAdsAdapterTypeFactory() {
    }

    @Override
    public int type(BannerShopViewModel viewModel) {
        return BannerShopViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopProductViewModel viewModel) {
        return BannerShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == BannerShopViewHolder.LAYOUT) {
            holder = new BannerShopViewHolder(view);
        } else if (viewType == BannerShopProductViewHolder.LAYOUT) {
            holder = new BannerShopProductViewHolder(view);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
