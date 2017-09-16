package com.tokopedia.gm.statistic.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.view.adapter.viewholder.GMMarketViewHolder;

import java.util.List;

/**
 * Created by Nathan on 7/25/2017.
 */
public class GMMarketInsightAdapter extends RecyclerView.Adapter<GMMarketViewHolder> {

    private static final int MAX_KEYWORD_SHOWN = 3;

    List<GetKeyword.SearchKeyword> searchKeywords;

    public GMMarketInsightAdapter(List<GetKeyword.SearchKeyword> searchKeywords) {
        setSearchKeywords(searchKeywords);

    }

    public void setSearchKeywords(List<GetKeyword.SearchKeyword> searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    @Override
    public GMMarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_gm_statistic_dashboard_market_insight, parent, false);
        return new GMMarketViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(GMMarketViewHolder holder, int position) {
        holder.bindData(searchKeywords.get(position), searchKeywords);
    }

    @Override
    public int getItemCount() {
        return searchKeywords.size() >= MAX_KEYWORD_SHOWN ? MAX_KEYWORD_SHOWN : searchKeywords.size();
    }
}
