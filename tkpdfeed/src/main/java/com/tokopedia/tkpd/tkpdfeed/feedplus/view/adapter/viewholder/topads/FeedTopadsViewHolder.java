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

    TextView pos;

    public FeedTopadsViewHolder(View itemView, TopAdsItemClickListener topAdsItemClickListener) {
        super(itemView);
        pos = (TextView) itemView.findViewById(R.id.position);
    }

    @Override
    public void bind(FeedTopAdsViewModel element) {
        pos.setText("THIS IS TOPADS " + element.getPage() + " " + getAdapterPosition());

    }
}
