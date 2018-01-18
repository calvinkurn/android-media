package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenDomainView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopOpenDomainPresenter extends CustomerPresenter<ShopOpenDomainView> {
    
    void checkShop(String shopName);

    void checkDomain(String domainName);
}
