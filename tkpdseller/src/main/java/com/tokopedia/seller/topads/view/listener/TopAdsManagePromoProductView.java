package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsManagePromoProductView extends CustomerView {
    void onCheckGroupExistError(String message);

    void onGroupExist();

    void onGroupNotExist();
}
