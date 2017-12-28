package com.tokopedia.seller.shop.setting.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public interface ShopSettingLogisticView extends CustomerView {

    void onSuccessLoadLogistic(OpenShopCouriersModel openShopCouriersModel);

    void onErrorLoadLogistic(Throwable t);

    void onErrorSaveCourier(Throwable t);

    void onErrorCreateShop(Throwable t);

    void onSuccessCreateShop();

}
