package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewShopPresenter<T extends TopAdsDetailNewView> extends TopAdsDetailNewPresenter<T> {

    void saveAd(TopAdsDetailShopViewModel topAdsDetailShopViewModel);
}
