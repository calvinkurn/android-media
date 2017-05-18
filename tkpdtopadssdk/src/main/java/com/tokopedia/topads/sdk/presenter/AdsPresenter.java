package com.tokopedia.topads.sdk.presenter;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.view.AdsView;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public interface AdsPresenter <V extends AdsView>{

    void attachView(V view);

    void detachView();

    boolean isViewAttached();

    void checkViewAttached();

    void setParams(TopAdsParams adsParams);

    TopAdsParams getTopAdsParam();

    void setMaxItems(int items);

    void setEndpoinParam(String ep);

    void loadTopAds();

    void openProductTopAds(String click_url, Product product);

    void openShopTopAds(String click_url, Shop shop);

    void setDisplayMode(int displayMode);

    void getPreferedCategory();

    void setConfig(Config config);
}
