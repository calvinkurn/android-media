package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.topads.keyword.view.adapter.viewholder.TopAdsKeywordViewHolder;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.model.NegativeKeywordAd;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsKeywordAdapter extends BaseListAdapter<KeywordAd> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case KeywordAd.TYPE:
            case NegativeKeywordAd.TYPE:
                return new TopAdsKeywordViewHolder(getLayoutView(parent, R.layout.item_top_ads_keyword_main));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}