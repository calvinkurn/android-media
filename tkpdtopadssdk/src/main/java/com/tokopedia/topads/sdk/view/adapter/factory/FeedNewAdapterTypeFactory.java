package com.tokopedia.topads.sdk.view.adapter.factory;

import android.content.Context;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feednew.ProductFeedNewViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feednew.ShopFeedNewViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ProductFeedNewViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ShopFeedNewViewModel;

/**
 * @author by milhamj on 29/03/18.
 */

public class FeedNewAdapterTypeFactory implements FeedNewTypeFactory {

    private int clickPosition;
    private LocalAdsClickListener itemClickListener;
    private ImageLoader imageLoader;

    public FeedNewAdapterTypeFactory(Context context, int clickPosition) {
        imageLoader = new ImageLoader(context);
        this.clickPosition = clickPosition;
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setClickPosition(int adapterPosition) {
        clickPosition = adapterPosition;
    }

    @Override
    public int type(ShopFeedNewViewModel viewModel) {
        return ShopFeedNewViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductFeedNewViewModel viewModel) {
        return ProductFeedNewViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == ProductFeedNewViewHolder.LAYOUT) {
            holder = new ProductFeedNewViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == ShopFeedNewViewHolder.LAYOUT) {
            holder = new ShopFeedNewViewHolder(view, imageLoader, itemClickListener);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
