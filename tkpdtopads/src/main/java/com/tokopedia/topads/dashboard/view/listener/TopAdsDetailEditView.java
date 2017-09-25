package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsDetailEditView extends TopAdsGetProductDetailView {

    void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onLoadDetailAdError(String errorMessage);

    void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onSaveAdError(String errorMessage);
}
