package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsManagePromoProductView;

/**
 * Created by zulfikarrahman on 2/16/17.
 */
public interface TopAdsManagePromoProductPresenter extends CustomerPresenter<TopAdsManagePromoProductView>{
    void checkIsGroupExist(String keyword);
    void searchGroupName(String keyword);

    void checkIsGroupExistOnSubmitNewGroup(String keyword);
}
