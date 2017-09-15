package com.tokopedia.seller.goldmerchant.statistic.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.viewholder.MarketViewHolder;

import java.util.List;

/**
 * Created by Nathan on 7/25/2017.
 */
public class MarketInsightAdapter extends RecyclerView.Adapter<MarketViewHolder> {

    private static final int MAX_KEYWORD_SHOWN = 3;

    List<GetKeyword.SearchKeyword> searchKeywords;

    public MarketInsightAdapter(List<GetKeyword.SearchKeyword> searchKeywords) {
        setSearchKeywords(searchKeywords);

    }

    public void setSearchKeywords(List<GetKeyword.SearchKeyword> searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    @Override
    public MarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_gm_statistic_dashboard_market_insight, parent, false);
        return new MarketViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MarketViewHolder holder, int position) {
        holder.bindData(searchKeywords.get(position), searchKeywords);
    }

    @Override
    public int getItemCount() {
        return searchKeywords.size() >= MAX_KEYWORD_SHOWN ? MAX_KEYWORD_SHOWN : searchKeywords.size();
    }
}
