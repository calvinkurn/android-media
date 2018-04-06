package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewViewHolder extends AbstractViewHolder<RecentViewViewModel> {
    private static final int SPAN_COUNT = 3;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recent_view_product;

    private RecyclerView recyclerView;
    private TextView seeAllButton;
    private HistoryProductRecyclerViewAdapter adapter;


    public RecentViewViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.history_product_recycler_view);
        seeAllButton = (TextView) itemView.findViewById(R.id.see_all);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSeeAllRecentView();
            }
        });

        final LinearLayoutManager layoutManager = new GridLayoutManager(
                itemView.getContext(),
                SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryProductRecyclerViewAdapter(viewListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(RecentViewViewModel element) {
        adapter.setData(element.getListProduct());
        adapter.notifyDataSetChanged();
    }
}
