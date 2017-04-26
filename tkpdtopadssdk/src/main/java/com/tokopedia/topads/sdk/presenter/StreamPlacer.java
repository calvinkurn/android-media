package com.tokopedia.topads.sdk.presenter;

import android.content.Context;
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
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author by errysuprayogi on 4/18/17.
 */

public class StreamPlacer implements AdsView, LocalAdsClickListener {

    private static final String TAG = StreamPlacer.class.getSimpleName();
    private TopAdsPresenter presenter;
    private int ajustedPositionStart = 0;
    private int ajustedItemCount = 0;
    private int observerType;
    private TopAdsListener topAdsListener;
    private TopAdsItemClickListener adsItemClickListener;
    private DataObserver observer;
    private TopAdsParams adsParams;
    private List<Item> items = new ArrayList<>();
    private int mPage = 1;
    private boolean hasHeader = false;
    private boolean headerPlaced = false;

    public StreamPlacer(
            Context context, TopAdsAdapterTypeFactory typeFactory, DataObserver observer) {
        presenter = new TopAdsPresenter(context);
        adsParams = new TopAdsParams();
        this.observer = observer;
        typeFactory.setItemClickListener(this);
        initPresenter();
    }

    public void setAjustedItemCount(int ajustedItemCount) {
        this.ajustedItemCount = ajustedItemCount;
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

    public void onChanged() {
        observerType = ObserverType.CHANGE;
        if (hasHeader)
            headerPlaced = false;
        loadTopAds();
    }

    public void onItemRangeInserted(final int positionStart, final int itemCount) {
        ajustedPositionStart = positionStart;
        ajustedItemCount = itemCount;
        observerType = ObserverType.ITEM_RANGE_INSERTED;
        loadTopAds();
    }

    @Override
    public void initPresenter() {
        presenter.attachView(this);
    }

    @Override
    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    @Override
    public void setTopAdsParams(TopAdsParams adsParams) {
        this.adsParams = adsParams;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    @Override
    public void setDisplayMode(int displayMode) {
        presenter.setDisplayMode(displayMode);
        for (Item visitable : items) {
            if (visitable instanceof TopAdsViewModel) {
                TopAdsViewModel topAdsViewModel = (TopAdsViewModel) visitable;
                topAdsViewModel.switchDisplayMode(displayMode);
            }
        }
    }

    public int getDisplayMode() {
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

    private void reset(){
        ajustedPositionStart = 0;
        mPage = 1;
        items.clear();
    }

    @Override
    public void loadTopAds() {
        adsParams.getParam().put(TopAdsParams.KEY_PAGE, String.valueOf(mPage));
        presenter.setParams(adsParams);
        presenter.loadTopAds();
    }

    @Override
    public void displayAds(List<Item> list) {
        switch (observerType) {
            case ObserverType.CHANGE:
                reset();
                renderItems(list, ajustedPositionStart, ajustedItemCount);
                break;
            case ObserverType.ITEM_RANGE_INSERTED:
                renderItems(list, ajustedPositionStart, (ajustedPositionStart + ajustedItemCount));
                break;
        }
        observer.onStreamLoaded(observerType);
        if (topAdsListener != null) {
            topAdsListener.onTopAdsLoaded();
        }
    }

    private void renderItems(List<Item> list, int positionStart, int itemCount) {
        ArrayList<Item> arrayList = new ArrayList<>();
        Log.d(TAG, "start " + positionStart + " item count " + itemCount);
        for (int i = positionStart; i < itemCount; i++) {
            ClientViewModel model = new ClientViewModel();
            model.setPosition(i);
            Log.d(TAG, "add new item pos " + i);
            arrayList.add(model);
        }
        if (hasHeader && !headerPlaced) {
            setTopAds(list, arrayList, 1);
        } else {
            setTopAds(list, arrayList, 0);
        }
        ajustedPositionStart = getItemCount();
        ajustedItemCount = arrayList.size();
        items.addAll(arrayList);
        headerPlaced = true;
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        Log.e(TAG, "Ads failed to load error message "+message);
        renderItems(Collections.<Item>emptyList(), ajustedPositionStart, (ajustedPositionStart + ajustedItemCount));
        observer.onStreamLoaded(observerType);
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

    @Override
    public void initLoading() {

    }

    @Override
    public void finishLoading() {

    }

    @Override
    public void showLoading(boolean showLoading) {

    }

    public void setConfig(Config config) {
        presenter.setConfig(config);
    }

    private void setTopAds(List<Item> list, ArrayList<Item> arrayList, int pos) {
        if (list.size() > 0) {
            arrayList.add(pos, new TopAdsViewModel(list));
            mPage++;
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
        if (adsItemClickListener != null) {
            adsItemClickListener.onAddFavorite(shop);
        }
    }

    public interface DataObserver {
        void onStreamLoaded(int type);
    }
}
