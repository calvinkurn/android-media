package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditShopPresenter extends TopAdsDetailNewShopPresenter<TopAdsDetailEditView> {

    void getDetailAd(String adId);

}
