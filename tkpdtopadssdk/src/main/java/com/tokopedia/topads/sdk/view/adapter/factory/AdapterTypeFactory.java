package com.tokopedia.topads.sdk.view.adapter.factory;

import android.content.Context;
import android.view.View;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.imageutils.ImageCache;
import com.tokopedia.topads.sdk.imageutils.ImageFetcher;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewholder.ProductGridViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.ProductListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.ShopGridViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.ShopListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopListViewModel;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public class AdapterTypeFactory implements TypeFactory {

    private LocalAdsClickListener itemClickListener;
    private ImageLoader imageLoader;
    public AdapterTypeFactory(Context context) {
        imageLoader = new ImageLoader(context);
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int type(ProductGridViewModel viewModel) {
        return ProductGridViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductListViewModel viewModel) {
        return ProductListViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopGridViewModel viewModel) {
        return ShopGridViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopListViewModel viewModel) {
        return ShopListViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == ProductGridViewHolder.LAYOUT) {
            holder = new ProductGridViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == ProductListViewHolder.LAYOUT) {
            holder = new ProductListViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == ShopGridViewHolder.LAYOUT) {
            holder = new ShopGridViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == ShopListViewHolder.LAYOUT) {
            holder = new ShopListViewHolder(view, imageLoader, itemClickListener);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
