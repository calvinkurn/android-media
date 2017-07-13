package com.tokopedia.seller.topads.dashboard.view.presenter;

import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditShopPresenter<T extends TopAdsDetailEditView> extends TopAdsDetailEditPresenter<T> {

    void saveAd(TopAdsDetailShopViewModel topAdsDetailShopViewModel);
}
