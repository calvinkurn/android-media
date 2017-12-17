package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.shop.open.view.listener.ShopCheckDomainView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopCheckIsReservePresenter extends CustomerPresenter<ShopCheckDomainView> {
    
    void isReservingDomain();
}
