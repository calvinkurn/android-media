package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;

import java.util.List;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsManagePromoProductView extends CustomerView {
    void onCheckGroupExistError(String message);

    void onGroupExist();

    void onGroupNotExist();

    void onGetGroupAdList(List<GroupAd> groupAds);

    void onGetGroupAdListError(String message);
}
