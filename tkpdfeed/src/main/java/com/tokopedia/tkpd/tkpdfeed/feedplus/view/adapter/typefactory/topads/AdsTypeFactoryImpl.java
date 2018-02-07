package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads;

import android.content.Context;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads.ProductFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads.ShopFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.LocalAdsClickListener;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ProductFeedTopAdsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ShopFeedTopAdsViewModel;

/**
 * Created by milhamj on 18/01/18.
 */

public class AdsTypeFactoryImpl implements AdsTypeFactory {

    private int clickPosition;
    private LocalAdsClickListener itemClickListener;

    public AdsTypeFactoryImpl(Context context) {
        this(context, 0);
    }

    public AdsTypeFactoryImpl(Context context, int clickPosition) {
        this.clickPosition = clickPosition;
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
    }

    @Override
    public int type(ShopFeedTopAdsViewModel viewModel) {
        return ShopFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductFeedTopAdsViewModel viewModel) {
        return ProductFeedViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == ShopFeedViewHolder.LAYOUT) {
            holder = new ShopFeedViewHolder(view, itemClickListener);
        } else if (viewType == ProductFeedViewHolder.LAYOUT) {
            holder = new ProductFeedViewHolder(view, itemClickListener);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
