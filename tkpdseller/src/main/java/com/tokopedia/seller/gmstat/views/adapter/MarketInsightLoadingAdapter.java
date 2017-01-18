package com.tokopedia.seller.gmstat.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderTextView;
import com.tokopedia.seller.gmstat.views.GMStatActivityFragment;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class MarketInsightLoadingAdapter extends RecyclerView.Adapter{
    public MarketInsightLoadingAdapter(){
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.market_insight_item_layout_loading, parent, false);
        return new ViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // do nothing
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ViewHolder3 extends RecyclerView.ViewHolder{
        LoaderTextView marketInsightKeywordLoading;

        LoaderTextView marketInsightNumberLoading;

        void initView(View itemView){
            marketInsightKeywordLoading= (LoaderTextView) itemView.findViewById(R.id.market_insight_keyword_loading);

            marketInsightNumberLoading= (LoaderTextView) itemView.findViewById(R.id.market_insight_number_loading);
        }

        public ViewHolder3(View itemView) {
            super(itemView);
            initView(itemView);

            marketInsightKeywordLoading.resetLoader();
            marketInsightNumberLoading.resetLoader();
        }
    }
}
