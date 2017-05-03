package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditPresenter<T extends TopAdsDetailEditView> extends CustomerPresenter<T> {

    void getDetailAd(String adId);

}
