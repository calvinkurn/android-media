package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsDetailEditView extends TopAdsGetProductDetailView {

    void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onLoadDetailAdError(String errorMessage);

    void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onSaveAdError(String errorMessage);
}
