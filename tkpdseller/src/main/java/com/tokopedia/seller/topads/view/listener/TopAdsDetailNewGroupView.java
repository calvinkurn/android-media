package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;

/**
 * Created by hendry on 3/3/17.
 */
public interface TopAdsDetailNewGroupView extends TopAdsDetailEditView {
    void showErrorGroupEmpty();
    void showLoading(boolean isShown);
}
