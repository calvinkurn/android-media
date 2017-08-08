package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.ObserverType;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 4/18/17.
 */

public class TopAdsPlacer implements AdsView, LocalAdsClickListener {

    private static final String TAG = TopAdsPlacer.class.getSimpleName();
    private TopAdsPresenter presenter;
    private int ajustedPositionStart = 0;
    private int ajustedItemCount = 0;
    private int observerType;
    private TopAdsListener topAdsListener;
    private TopAdsItemClickListener adsItemClickListener;
    private DataObserver observer;
    private List<Item> items = new ArrayList<>();
    private int mPage = 1;
    private boolean hasHeader = false;
    private boolean headerPlaced = false;
    private boolean isFeed = false;
    private boolean mNeedsPlacement;
    private boolean shouldLoadAds = true; //default load ads
    private int adsPos = 0;
    private final RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    public TopAdsPlacer(RecyclerView.Adapter adapter, Context context,
                        TopAdsAdapterTypeFactory typeFactory, DataObserver observer) {
        presenter = new TopAdsPresenter(context);
        this.adapter = adapter;
        this.observer = observer;
        typeFactory.setItemClickListener(this);
        initPresenter();
    }

    public void setAjustedItemCount(int ajustedItemCount) {
        this.ajustedItemCount = ajustedItemCount;
    }

    public int getPositionStart(int positionStart) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof ClientViewModel && item.originalPos() == positionStart) {
                positionStart = i;
                break;
            }
        }
        return positionStart;
    }

    public int getAjustedPositionStart() {
        return ajustedPositionStart;
    }

    public int getAjustedItemCount() {
        return ajustedItemCount;
    }

    public void setTopAdsListener(TopAdsListener topAdsListener) {
        this.topAdsListener = topAdsListener;
    }

    public void setShouldLoadAds(boolean shouldLoadAds) {
        this.shouldLoadAds = shouldLoadAds;
    }

    public void onChanged() {
        if (mNeedsPlacement)
            return;
        mNeedsPlacement = true;
        observerType = ObserverType.CHANGE;
        if (hasHeader)
            reset();
        headerPlaced = false;
        if (shouldLoadAds) {
            loadTopAds();
        }
        renderItem(ajustedPositionStart, ajustedItemCount);
    }

    public void onItemRangeInserted(final int positionStart, final int itemCount) {
        ajustedPositionStart = positionStart;
        ajustedItemCount = itemCount;
        observerType = ObserverType.ITEM_RANGE_INSERTED;
        if (shouldLoadAds) {
            loadTopAds();
        }
        renderItem(ajustedPositionStart, (ajustedPositionStart + ajustedItemCount));
    }

    @Override
    public void initPresenter() {
        presenter.attachView(this);
    }

    @Override
    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        presenter.setDisplayMode(displayMode);
        for (Item visitable : items) {
            if (visitable instanceof TopAdsViewModel) {
                TopAdsViewModel topAdsViewModel = (TopAdsViewModel) visitable;
                topAdsViewModel.switchDisplayMode(displayMode);
            }
        }
    }

    public DisplayMode getDisplayMode() {
        return presenter.getDisplayMode();
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getPage() {
        return mPage;
    }

    public void reset() {
        ajustedPositionStart = 0;
        mPage = 1;
        items.clear();
        setShouldLoadAds(true);
    }

    @Override
    public void loadTopAds() {
        presenter.getTopAdsParam().getParam().put(TopAdsParams.KEY_PAGE, String.valueOf(mPage));
        presenter.loadTopAds();
    }

    public void attachRecycleView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void displayAds(List<Item> list) {
        Log.d(TAG, "displayAds list size " + list.size());
        if (isFeed) {
            setTopAds(list, items, getItemCount());
        } else {
            if (hasHeader && !headerPlaced) {
                headerPlaced = true;
                setTopAds(list, items, 1);
                adapter.notifyItemInserted(1);
            } else {
                if (headerPlaced || ajustedPositionStart > 0) {
                    adsPos = ajustedPositionStart - 1;
                } else {
                    adsPos = ajustedPositionStart;
                }
                setTopAds(list, items, adsPos);
                adapter.notifyItemInserted(adsPos);
                if (recyclerView != null && adsPos == 0) {
                    recyclerView.scrollToPosition(adsPos);
                }
            }
        }
    }

    private void renderItem(int positionStart, int itemCount) {
        Log.d(TAG, "renderItem start " + positionStart + " item count " + itemCount);
        ArrayList<Item> arrayList = new ArrayList<>();
        for (int i = positionStart; i < itemCount; i++) {
            ClientViewModel model = new ClientViewModel();
            model.setPosition(i);
            Log.d(TAG, "add new item pos " + i);
            arrayList.add(model);
        }
        ajustedPositionStart = getItemCount();
        items.addAll(arrayList);
        mNeedsPlacement = false;
        observer.onStreamLoaded(observerType);
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        Log.e(TAG, "Ads failed to load error message " + message);
        if (topAdsListener != null) {
            topAdsListener.onTopAdsFailToLoad(errorCode, message);
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

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
    }

    public void setConfig(Config config) {
        if (config.getDisplayMode() == DisplayMode.FEED) {
            isFeed = true;
        }
        presenter.setConfig(config);
    }

    public Config getConfig() {
        return presenter.getConfig();
    }

    private void setTopAds(List<Item> list, List<Item> arrayList, int pos) {
        Log.d(TAG, "setTopAds size " + list.size() + " pos " + pos);
        if (list.size() > 0) {
            arrayList.add(pos, new TopAdsViewModel(list));
        } else {
            setShouldLoadAds(false);
        }
    }

    public void increasePage() {
        mPage++;
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


    public interface DataObserver {
        void onStreamLoaded(int type);
    }
}
