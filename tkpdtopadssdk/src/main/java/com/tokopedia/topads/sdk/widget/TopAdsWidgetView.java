package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.utils.DividerItemDecoration;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 2/20/18.
 */

public class TopAdsWidgetView extends LinearLayout implements LocalAdsClickListener {

    private static final String TAG = TopAdsWidgetView.class.getSimpleName();
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private LinearLayout adsHeader;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private DividerItemDecoration itemDecoration;
    private List<Data> data = new ArrayList<>();
    private TopAdsItemClickListener itemClickListener;

    public TopAdsWidgetView(Context context) {
        super(context);
        inflateView(context, null, 0);
    }

    public TopAdsWidgetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
    }

    public TopAdsWidgetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_ads, this);
        adapter = new AdsItemAdapter(getContext());
        adapter.setItemClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));
        itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<Data> data) {
        this.data = data;
        List<Item> visitables = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            if (d.getProduct() != null) {
                visitables.add(ModelConverter.convertToProductFeedViewModel(d));
            } else if (d.getShop() != null) {
                visitables.add(ModelConverter.convertToShopFeedViewModel(d, DisplayMode.FEED));
            }
        }
        adapter.setList(visitables);
    }
    
    @Override
    public void onShopItemClicked(int position, Data data) {
        itemClickListener.onShopItemClicked(data.getShop());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        itemClickListener.onProductItemClicked(data.getProduct());
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        itemClickListener.onAddFavorite(position, dataShop);
    }

    public void setItemClickListener(TopAdsItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
