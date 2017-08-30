package com.tokopedia.seller.topads.dashboard.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsEtalaseListView;

/**
 * Created by hendry on 2/27/17.
 */
public interface TopAdsEtalaseListPresenter extends CustomerPresenter<TopAdsEtalaseListView> {

    void populateEtalaseList(String shopId);
}
