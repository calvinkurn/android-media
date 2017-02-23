package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsEtalaseListView;
import com.tokopedia.seller.topads.view.listener.TopAdsManagePromoProductView;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsEtalaseListPresenter extends CustomerPresenter<TopAdsEtalaseListView> {

    void populateEtalaseList(String shopId);
}
