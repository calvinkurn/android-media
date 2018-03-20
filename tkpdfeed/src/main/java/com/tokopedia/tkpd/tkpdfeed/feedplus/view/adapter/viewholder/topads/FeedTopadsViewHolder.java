package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsWidgetView;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopadsViewHolder extends AbstractViewHolder<FeedTopAdsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_topads_layout;
    private TopAdsWidgetView topAdsWidgetView;

    public FeedTopadsViewHolder(View itemView,
                                TopAdsItemClickListener itemClickListener) {
        super(itemView);
        topAdsWidgetView = (TopAdsWidgetView) itemView;
        topAdsWidgetView.setItemClickListener(itemClickListener);
    }

    @Override
    public void bind(FeedTopAdsViewModel element) {
        topAdsWidgetView.setData(element.getList());
        topAdsWidgetView.setAdapterPosition(getAdapterPosition());
    }
}
