package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.logistic.model.CouriersModel;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public interface ShopOpenLogisticView extends CustomerView {

    void onSuccessLoadLogistic(CouriersModel couriersModel);

    void onErrorLoadLogistic(Throwable t);

    void onErrorSaveCourier(Throwable t);

    void onErrorCreateShop(Throwable t);

    void onSuccessCreateShop();

}
