package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsGetProductDetailView extends CustomerView {

    void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel);
}
