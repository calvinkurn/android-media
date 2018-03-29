package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsInfoBottomSheet;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 4/13/17.
 */

public class
TopAdsViewHolder extends AbstractViewHolder<TopAdsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads;
    private LinearLayout container;
    private static final String TAG = TopAdsViewHolder.class.getSimpleName();
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private Context context;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private DisplayMode displayMode;
    private TopAdsInfoClickListener clickListener;
    private TextView textHeader;

    public TopAdsViewHolder(View itemView, LocalAdsClickListener itemClickListener) {
        super(itemView);
        context = itemView.getContext();
        recyclerView = (RecyclerView) itemView.findViewById(R.id.list);
        gridLayoutManager = new GridLayoutManager(context, DEFAULT_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(context);
        container = (LinearLayout) itemView.findViewById(R.id.root);
        adapter = new AdsItemAdapter(context);
        adapter.setItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(TopAdsViewModel element) {
        List<Item> list = element.getList();
        adapter.setList(list);
        adapter.setAdapterPosition(getAdapterPosition());
        adapter.setPosition(getAdapterPosition());
        if (list.size() > 0) {
            switchDisplay(list.get(0));
        }
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    private void switchDisplay(Item item) {
        switch (displayMode) {
            case FEED:
                if (item instanceof ShopFeedViewModel) {
                    recyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    recyclerView.setLayoutManager(gridLayoutManager);
                }
                recyclerView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                break;
            case GRID:
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                break;
            case LIST:
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                break;
        }
    }

    public void setClickListener(TopAdsInfoClickListener adsInfoClickListener) {
        clickListener = adsInfoClickListener;
    }
}
