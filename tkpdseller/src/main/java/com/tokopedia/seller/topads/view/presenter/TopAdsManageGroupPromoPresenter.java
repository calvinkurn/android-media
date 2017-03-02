package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsManageGroupPromoView;

/**
 * Created by zulfikarrahman on 2/16/17.
 */
public interface TopAdsManageGroupPromoPresenter<T extends TopAdsManageGroupPromoView> extends CustomerPresenter<T>{
    void checkIsGroupExist(String keyword);
    void searchGroupName(String keyword);

    void checkIsGroupExistOnSubmitNewGroup(String keyword);
}
