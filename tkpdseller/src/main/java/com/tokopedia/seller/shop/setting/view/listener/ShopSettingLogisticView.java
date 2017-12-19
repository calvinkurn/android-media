package com.tokopedia.seller.shop.setting.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shop.open.data.model.OpenShopLogisticModel;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public interface ShopSettingLogisticView extends CustomerView {

    void onSuccessLoadLogistic(OpenShopLogisticModel openShopLogisticModel);

    void onErrorLoadLogistic(Throwable t);

}
