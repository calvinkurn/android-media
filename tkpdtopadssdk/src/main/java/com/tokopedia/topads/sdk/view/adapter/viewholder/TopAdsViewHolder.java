package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsInfoDialog;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 4/13/17.
 */

public class TopAdsViewHolder extends AbstractViewHolder<TopAdsViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads;
    private View container;

    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private LinearLayout adsHeader;
    private Context context;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private DisplayMode displayMode;
    private TopAdsInfoClickListener clickListener;

    public TopAdsViewHolder(View itemView, LocalAdsClickListener itemClickListener) {
        super(itemView);
        context = itemView.getContext();
        adsHeader = (LinearLayout) itemView.findViewById(R.id.ads_header);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.list);
        gridLayoutManager = new GridLayoutManager(context, DEFAULT_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(context);
        itemView.findViewById(R.id.info_topads).setOnClickListener(this);
        container = itemView.findViewById(R.id.container);
        adapter = new AdsItemAdapter(context);
        adapter.setItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(TopAdsViewModel element) {
        List<Item> list = element.getList();
        if (list.size() > 0) {
            adsHeader.setVisibility(View.VISIBLE);
            switchDisplay(list.get(0));
        }
        adapter.setList(list);
        adapter.setPosition(getAdapterPosition());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_topads) {
            if(clickListener != null){
                clickListener.onInfoClicked();
            }else {
                TopAdsInfoDialog infoTopAds = TopAdsInfoDialog.newInstance();
                Activity activity = (Activity) context;
                infoTopAds.show(activity.getFragmentManager(), "INFO_TOPADS");
            }
        }
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    private void switchDisplay(Item item){
        switch (displayMode) {
            case FEED:
                if(item instanceof ShopFeedViewModel){
                    recyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    recyclerView.setLayoutManager(gridLayoutManager);
                }
                TextView textView = (TextView) adsHeader.findViewById(R.id.title_promote);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getContext().getResources().getDimension(R.dimen.font_small));
                ImageView imageView = (ImageView) adsHeader.findViewById(R.id.info_topads);
                imageView.setImageResource(R.drawable.icon_info);
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.tkpd_dark_gray));
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)container.getLayoutParams();


                params.setMargins(0, params.topMargin, 0, 0);
                container.setLayoutParams(params);
                break;
            case GRID:
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case LIST:
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
        }
    }

    public void setClickListener(TopAdsInfoClickListener adsInfoClickListener) {
        clickListener = adsInfoClickListener;
    }
}
