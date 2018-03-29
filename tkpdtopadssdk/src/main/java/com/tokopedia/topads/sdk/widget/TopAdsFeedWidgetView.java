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
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.FeedNewAdsItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class TopAdsFeedWidgetView extends LinearLayout implements LocalAdsClickListener {

    private static final String TAG = TopAdsFeedWidgetView.class.getSimpleName();
    private FeedNewAdsItemAdapter adapter;
    private static final int DEFAULT_SPAN_COUNT = 3;
    private TopAdsItemClickListener itemClickListener;
    private OpenTopAdsUseCase openTopAdsUseCase;
    private GridLayoutManager layoutManager;

    public TopAdsFeedWidgetView(Context context) {
        super(context);
        inflateView(context, null, 0);
    }

    public TopAdsFeedWidgetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
    }

    public TopAdsFeedWidgetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_ads, this);
        openTopAdsUseCase = new OpenTopAdsUseCase(context);
        adapter = new FeedNewAdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        layoutManager = new GridLayoutManager(getContext(),
                DEFAULT_SPAN_COUNT,
                GridLayoutManager.HORIZONTAL,
                false);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<Data> data) {
        List<Item> visitables = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            if (d.getProduct() != null) {
                layoutManager.setSpanCount(1);
                visitables.add(ModelConverter.convertToProductFeedNewViewModel(d));
            } else if (d.getShop() != null) {
                layoutManager.setSpanCount(3);
                visitables.add(ModelConverter.convertToShopFeedNewViewModel(d));
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
        itemClickListener.onShopItemClicked(data.getShop());
        openTopAdsUseCase.execute(data.getShopClickUrl());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        itemClickListener.onProductItemClicked(data.getProduct());
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
