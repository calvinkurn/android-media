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
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
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
    private static final int DEFAULT_SPAN_COUNT = 2;
    private List<Data> data = new ArrayList<>();
    private TopAdsItemClickListener itemClickListener;
    private OpenTopAdsUseCase openTopAdsUseCase;
    private GridLayoutManager layoutManager;

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
        openTopAdsUseCase = new OpenTopAdsUseCase(context);
        adapter = new AdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        layoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT,
                        GridLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<Data> data) {
        this.data = data;
        List<Item> visitables = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            if (d.getProduct() != null) {
                layoutManager.setSpanCount(2);
                visitables.add(ModelConverter.convertToProductFeedViewModel(d));
            } else if (d.getShop() != null) {
                layoutManager.setSpanCount(1);
                visitables.add(ModelConverter.convertToShopFeedViewModel(d, DisplayMode.FEED));
            }
        }
        adapter.setList(visitables);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        openTopAdsUseCase.unsubscribe();
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        Shop shop = data.getShop();
        shop.setAdRefKey(data.getAdRefKey());
        itemClickListener.onShopItemClicked(shop);
        openTopAdsUseCase.execute(data.getShopClickUrl());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        Product product = data.getProduct();
        product.setAdRefKey(data.getAdRefKey());
        itemClickListener.onProductItemClicked(product);
        openTopAdsUseCase.execute(data.getProductClickUrl());
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        itemClickListener.onAddFavorite(position, dataShop);
    }

    public void setItemClickListener(TopAdsItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    public void setAdapterPosition(int adapterPosition) {
        adapter.setAdapterPosition(adapterPosition);
    }
}
