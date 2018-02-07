package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads;

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

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.AdsItemAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.TopAdsInfoBottomSheet;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.LocalAdsClickListener;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ShopFeedTopAdsViewModel;

import java.util.List;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopadsViewHolder extends AbstractViewHolder<FeedTopAdsViewModel>
        implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_topads_layout;
    private LinearLayout container;
    private static final String TAG = FeedTopadsViewHolder.class.getSimpleName();
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private LinearLayout adsHeader;
    private Context context;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private LocalAdsClickListener clickListener;
    private TextView textHeader;

    public FeedTopadsViewHolder(View itemView, LocalAdsClickListener itemClickListener) {
        super(itemView);
        context = itemView.getContext();
        adsHeader = (LinearLayout) itemView.findViewById(R.id.ads_header);
        textHeader = (TextView) adsHeader.findViewById(R.id.title_promote);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.list);
        gridLayoutManager = new GridLayoutManager(context, DEFAULT_SPAN_COUNT,
                GridLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(context);
        itemView.findViewById(R.id.info_topads).setOnClickListener(this);
        container = (LinearLayout) itemView.findViewById(R.id.root);
        adapter = new AdsItemAdapter(context);
        adapter.setItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(FeedTopAdsViewModel element) {
        List<Visitable> list = element.getList();
        if (list.size() > 0) {
            adsHeader.setVisibility(View.VISIBLE);
            switchDisplay(list.get(0));
        }
        adapter.setList(list);
        adapter.setAdapterPosition(getAdapterPosition());
        adapter.setPosition(getAdapterPosition());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_topads) {
            Log.d(TAG, "Adapter Position " + getAdapterPosition());
            if (clickListener != null) {
                clickListener.onInfoClicked();
            } else {
                TopAdsInfoBottomSheet infoBottomSheet = TopAdsInfoBottomSheet.newInstance(context);
                infoBottomSheet.show();
            }
        }
    }

    private void switchDisplay(Visitable item) {
        if (item instanceof ShopFeedTopAdsViewModel) {
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        TextView textView = (TextView) adsHeader.findViewById(R.id.title_promote);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getContext().getResources().getDimension(R.dimen.font_small));
        ImageView imageView = (ImageView) adsHeader.findViewById(R.id.info_topads);
        imageView.setImageResource(R.drawable.icon_info);
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.tkpd_dark_gray));
        textHeader.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) container.getLayoutParams();
        params.setMargins(0, params.topMargin, 0, 0);
        container.setLayoutParams(params);
        recyclerView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    private void removeHeader() {
        adsHeader.setVisibility(View.GONE);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) container.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        container.setLayoutParams(params);
    }
}
