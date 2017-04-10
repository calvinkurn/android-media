package com.tokopedia.topads.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Visitable;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.utils.DividerItemDecoration;
import com.tokopedia.topads.sdk.view.adapter.TopAdsItemAdapter;

import java.util.List;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class TopAdsView extends LinearLayout implements AdsView, LocalAdsClickListener,
        View.OnClickListener {

    private static final String TAG = TopAdsView.class.getSimpleName();
    private TopAdsPresenter presenter;
    private RecyclerView recyclerView;
    private TopAdsItemAdapter adapter;
    private LinearLayout adsHeader;
    private TypedArray styledAttributes;
    private int displayMode = DisplayMode.GRID; // Default Display Mode
    private TopAdsItemClickListener adsItemClickListener;
    private DividerItemDecoration itemDecoration;
    private RelativeLayout contentLayout;
    private static final int DEFAULT_SPAN_COUNT = 2;
    private boolean isLoading = false;
    private boolean showLoading = false;
    private TopAdsListener adsListener;

    public TopAdsView(Context context) {
        super(context);
        inflateView(context, null, 0);
        initPresenter();
    }

    public TopAdsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
        initPresenter();
    }

    public TopAdsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
        initPresenter();
    }

    @Override
    public void inflateView(Context context, AttributeSet attrs, int defStyle) {
        styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsView, defStyle, 0);
        showLoading = styledAttributes.getBoolean(R.styleable.TopAdsView_show_loading, false);
        inflate(getContext(), R.layout.layout_ads, this);
        adsHeader = (LinearLayout) findViewById(R.id.ads_header);
        adapter = new TopAdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        ImageView infoView = (ImageView) findViewById(R.id.info_topads);
        infoView.setOnClickListener(this);
        contentLayout = (RelativeLayout) findViewById(R.id.container);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));
        itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.info_topads){
            TopAdsInfoDialog infoTopAds = TopAdsInfoDialog.newInstance();
            Activity activity = (Activity) getContext();
            infoTopAds.show(activity.getFragmentManager(), "INFO_TOPADS");
        }
    }

    @Override
    public void setSessionId(String sessionId) {
        presenter.setSessionId(sessionId);
    }

    @Override
    public void initPresenter() {
        presenter = new TopAdsPresenter(getContext());
        presenter.attachView(this);
        presenter.setMaxItems(styledAttributes.getInteger(R.styleable.TopAdsView_items, 2));
        presenter.setEndpoinParam(styledAttributes.getString(R.styleable.TopAdsView_ep));
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
    }

    @Override
    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    @Override
    public void showProduct() {
        setDisplayMode(DisplayMode.GRID);
        presenter.setEndpoinParam("1");
        adapter.clearData();
        presenter.loadTopAds();
    }

    @Override
    public void showShop() {
        setDisplayMode(DisplayMode.GRID);
        presenter.setEndpoinParam("2");
        adapter.clearData();
        presenter.loadTopAds();
    }

    @Override
    public void setTopAdsParams(TopAdsParams adsParams) {
        presenter.setParams(adsParams);
    }

    @Override
    public void loadTopAds() {
        presenter.loadTopAds();
    }

    @Override
    public void setDisplayMode(int displayMode) {
        switch (displayMode){
            case DisplayMode.GRID:
                itemDecoration.setOrientation(DividerItemDecoration.HORIZONTAL_LIST);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT,
                        GridLayoutManager.VERTICAL, false));
                break;
            case DisplayMode.LIST:
                itemDecoration.setOrientation(DividerItemDecoration.VERTICAL_LIST);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
        }
        this.displayMode = displayMode;
        adapter.switchDisplayMode(displayMode);
    }

    @Override
    public void displayAds(List<Visitable> list) {
        if(list.size()>0){
            adsHeader.setVisibility(VISIBLE);
        }
        adapter.setList(list);
        if(adsListener!=null){
            adsListener.onTopAdsLoaded();
        }
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        presenter.openShopTopAds(data.getShopClickUrl(), data.getShop());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        presenter.openProductTopAds(data.getProductClickUrl(), data.getProduct());

    }

    @Override
    public void onAddFavorite(int position, Shop shop) {
        if(adsItemClickListener !=null){
            adsItemClickListener.onAddFavorite(shop);
        }
    }

    @Override
    public void notifyProductClickListener(Product product) {
        if(adsItemClickListener !=null){
            adsItemClickListener.onProductItemClicked(product);
        }
    }

    @Override
    public void notifyShopClickListener(Shop shop) {
        if(adsItemClickListener !=null){
            adsItemClickListener.onShopItemClicked(shop);
        }
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if(adsListener!=null){
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    @Override
    public void initLoading() {
        adsHeader.setVisibility(GONE);
        if(!isLoading && showLoading) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_progress, contentLayout);
            isLoading = true;
        }
    }

    @Override
    public void finishLoading() {
        if(isLoading && showLoading){
            contentLayout.removeView(contentLayout.findViewById(R.id.progress_bar));
            isLoading = false;
        }
    }

    @Override
    public void showLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

}
