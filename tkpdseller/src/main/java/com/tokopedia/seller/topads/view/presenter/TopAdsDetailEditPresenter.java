package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoView;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditPresenter extends TopAdsDetailNewPresenter {

    void getDetailAd(String adId);
}
