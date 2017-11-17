package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopadsViewHolder extends AbstractViewHolder<FeedTopAdsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_topads_layout;


    public FeedTopadsViewHolder(View itemView, TopAdsItemClickListener topAdsItemClickListener) {
        super(itemView);
    }

    @Override
    public void bind(FeedTopAdsViewModel element) {

    }
}
