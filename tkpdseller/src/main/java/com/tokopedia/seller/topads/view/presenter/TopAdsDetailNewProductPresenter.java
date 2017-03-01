package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewProductPresenter<T extends TopAdsDetailNewView> extends TopAdsDetailNewPresenter<T> {

    void saveAd(TopAdsDetailProductViewModel topAdsDetailProductViewModel);
}
