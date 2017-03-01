package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsDetailEditView extends TopAdsDetailNewView {

    void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onLoadDetailAdError(String errorMessage);
}
