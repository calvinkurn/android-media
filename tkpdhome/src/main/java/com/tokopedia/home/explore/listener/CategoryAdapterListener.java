package com.tokopedia.home.explore.listener;


import com.tokopedia.home.explore.domain.model.LayoutRows;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface CategoryAdapterListener {
    void onMarketPlaceItemClicked(LayoutRows data);

    void onDigitalItemClicked(LayoutRows data);

    void onGimickItemClicked(LayoutRows data);

    void onApplinkClicked(LayoutRows data);

    void openShop();

    void onDigitalMoreClicked();

    void showNetworkError(String string);

    void openShopSetting();
}
