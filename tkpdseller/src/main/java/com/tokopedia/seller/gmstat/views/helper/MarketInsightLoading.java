package com.tokopedia.seller.gmstat.views.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;
import com.tokopedia.seller.gmstat.views.adapter.MarketInsightLoadingAdapter;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class MarketInsightLoading {
    private final View parentView;
    private LoaderImageView marketInsightHeaderIc;
    private LoaderTextView marketInsightHeaderText;
    private LoaderTextView marketInsightHeaderLoading;
    private RecyclerView marketInsightLoadingRec;

    public MarketInsightLoading(View itemView) {
        initView(itemView);

        parentView = itemView.findViewById(R.id.market_insight_loading);

        marketInsightHeaderIc.resetLoader();
        marketInsightHeaderText.resetLoader();
        marketInsightHeaderLoading.resetLoader();

        marketInsightLoadingRec.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        marketInsightLoadingRec.setAdapter(new MarketInsightLoadingAdapter());
    }

    private void initView(View itemView) {
        marketInsightHeaderIc = (LoaderImageView) itemView.findViewById(R.id.market_insight_header_ic);

        marketInsightHeaderText = (LoaderTextView) itemView.findViewById(R.id.market_insight_header_text);

        marketInsightHeaderLoading = (LoaderTextView) itemView.findViewById(R.id.market_insight_header_loading);

        marketInsightLoadingRec = (RecyclerView) itemView.findViewById(R.id.market_insight_loading_recyclerview);
    }

    public void displayLoading() {
        parentView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        parentView.setVisibility(View.GONE);
    }
}
