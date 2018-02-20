package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsWidgetView;



/**
 * Created by errysuprayogi on 2/20/18.
 */

public class TopAdsViewHolder extends AbstractViewHolder<TopAdsViewModel> implements TopAdsItemClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_ads;

    private TopAdsWidgetView topAdsWidgetView;

    public TopAdsViewHolder(View itemView) {
        super(itemView);
        topAdsWidgetView = (TopAdsWidgetView) itemView;
        topAdsWidgetView.setItemClickListener(this);
    }

    @Override
    public void bind(TopAdsViewModel element) {
        topAdsWidgetView.setData(element.getDataList());
    }

    @Override
    public void onProductItemClicked(Product product) {

    }

    @Override
    public void onShopItemClicked(Shop shop) {

    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }
}
