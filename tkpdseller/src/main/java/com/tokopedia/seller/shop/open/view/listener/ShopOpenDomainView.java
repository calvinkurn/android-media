package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopOpenDomainView extends CustomerView {

    boolean isShopNameInValidRange();

    void onSuccessCheckShopName(boolean existed);

    void onErrorCheckShopName(Throwable t);

    boolean isShopDomainInValidRange();

    void onSuccessCheckShopDomain(boolean existed);

    void onErrorCheckShopDomain(Throwable t);

    void onSuccessReserveShop(String shopName);

    void onErrorReserveShop(Throwable t);
}