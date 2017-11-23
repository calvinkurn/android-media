package com.tokopedia.topads.sdk.view;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;

import java.util.List;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public interface AdsView {

    void initPresenter();

    void setMaxItems(int items);

    void setDisplayMode(DisplayMode displayMode);

    void loadTopAds();

    void displayAds(List<Item> list, int position);

    void notifyAdsErrorLoaded(int errorCode, String message);

    void notifyProductClickListener(Product product);

    void notifyShopClickListener(Shop shop);

}
