package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractAdsViewHolder;
import com.tokopedia.topads.sdk.view.TopAdsView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class TopAdsViewHolder extends AbstractAdsViewHolder<TopAdsViewModel> {

    TopAdsView topAdsView;

    public TopAdsViewHolder(View itemView, Context context) {
        super(itemView, context);
        topAdsView = (TopAdsView) itemView.findViewById(R.id.ads);
    }

    @Override
    public void populateViewHolder(TopAdsViewModel data, int pos) {
        topAdsView.loadTopAds();
    }

    @Override
    public void attachOnClickListeners(AbstractAdsViewHolder viewHolder, TopAdsViewModel item, int pos) {

    }
}
