package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.util.AttributeSet;

import com.tokopedia.topads.sdk.base.adapter.Visitable;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;

import java.util.List;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public interface AdsView {

    void initPresenter();

    void inflateView(Context context, AttributeSet attrs, int defStyle);

    void setMaxItems(int items);

    void setTopAdsParams(TopAdsParams adsParams);

    void showProduct();

    void showShop();

    void setDisplayMode(int displayMode);

    void loadTopAds();

    void displayAds(List<Visitable> list);

    void notifyAdsErrorLoaded(int errorCode, String message);

    void notifyProductClickListener(Product product);

    void notifyShopClickListener(Shop shop);

    void initLoading();

    void finishLoading();

    void showLoading(boolean showLoading);

    void setSessionId(String sessionId);
}
