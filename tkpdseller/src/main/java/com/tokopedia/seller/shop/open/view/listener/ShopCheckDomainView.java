package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopCheckDomainView extends CustomerView {

    void onSuccessCheckReserveDomain(Object object);

    void onErrorCheckReserveDomain(Throwable t);

}