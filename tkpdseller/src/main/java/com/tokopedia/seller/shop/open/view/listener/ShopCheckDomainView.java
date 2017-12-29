package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shop.open.data.model.response.ResponseIsReserveDomain;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopCheckDomainView extends CustomerView {

    void onSuccessCheckReserveDomain(ResponseIsReserveDomain object);

    void onErrorCheckReserveDomain(Throwable t);

}