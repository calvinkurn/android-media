package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.utils.DividerItemDecoration;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class TopAdsView extends LinearLayout implements AdsView, LocalAdsClickListener {

    private static final String TAG = TopAdsView.class.getSimpleName();
    private TopAdsPresenter presenter;
    private RecyclerView recyclerView;
    private AdsItemAdapter adapter;
    private TypedArray styledAttributes;
    private DisplayMode displayMode = DisplayMode.GRID; // Default Display Mode
    private TopAdsItemClickListener adsItemClickListener;
    private DividerItemDecoration itemDecoration;
    private RelativeLayout contentLayout;
    private static final int DEFAULT_SPAN_COUNT = 2;
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

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsView, defStyle, 0);
        inflate(getContext(), R.layout.layout_ads, this);
        adapter = new AdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
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

    public void setConfig(Config config) {
        presenter.setConfig(config);
    }

    @Override
    public void initPresenter() {
        presenter = new TopAdsPresenter(getContext());
        presenter.attachView(this);
        presenter.setMaxItems(styledAttributes.getInteger(R.styleable.TopAdsView_items, 2));
        String ep = styledAttributes.getString(R.styleable.TopAdsView_ep);
        presenter.setEndpoinParam((ep == null ? "0" : ep));
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

    public void showProduct() {
        setDisplayMode(DisplayMode.GRID);
        presenter.setEndpoinParam("1");
        adapter.clearData();
        presenter.loadTopAds();
    }

    public void showShop() {
        setDisplayMode(DisplayMode.GRID);
        presenter.setEndpoinParam("2");
        adapter.clearData();
        presenter.loadTopAds();
    }

    @Override
    public void loadTopAds() {
        presenter.loadTopAds();
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        switch (displayMode) {
            case GRID:
                itemDecoration.setOrientation(DividerItemDecoration.HORIZONTAL_LIST);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT,
                        GridLayoutManager.VERTICAL, false));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case LIST:
                itemDecoration.setOrientation(DividerItemDecoration.VERTICAL_LIST);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case FEED:
                itemDecoration.setOrientation(DividerItemDecoration.HORIZONTAL_LIST);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT,
                        GridLayoutManager.VERTICAL, false));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case FEED_EMPTY:
                itemDecoration.setOrientation(DividerItemDecoration.VERTICAL_LIST);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                break;
        }
        this.displayMode = displayMode;
        presenter.setDisplayMode(displayMode);
        adapter.switchDisplayMode(displayMode);
    }

    @Override
    public void displayAds(List<Item> list, int position) {
        adapter.setList(list);
        if (adsListener != null) {
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
    public void onAddFavorite(int position, Data dataShop) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onAddFavorite(position, dataShop);
        }
    }

    @Override
    public void notifyProductClickListener(Product product) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onProductItemClicked(product);
        }
    }

    @Override
    public void notifyShopClickListener(Shop shop) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onShopItemClicked(shop);
        }
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void setFavoritedShop(int position, boolean b) {
        Item item = adapter.getItem(position);
        if (item instanceof ShopFeedViewModel) {
            ((ShopFeedViewModel) item).getData().setFavorit(b);
        }
        if (item instanceof ShopGridViewModel) {
            ((ShopGridViewModel) item).getData().setFavorit(b);
        }
        if (item instanceof ShopListViewModel) {
            ((ShopListViewModel) item).getData().setFavorit(b);
        }
        adapter.notifyItemChanged(position);
    }
}
