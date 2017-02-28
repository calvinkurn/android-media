package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditProductPresenter extends TopAdsDetailEditPresenter {

    void saveAd(TopAdsDetailProductViewModel topAdsDetailProductViewModel);
}
