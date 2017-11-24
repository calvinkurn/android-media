package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.app.BaseDiView;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopOpenDomainView extends CustomerView {

    void onSuccessCheckShopName(boolean existed);

    void onErrorCheckShopName(Throwable t);

    void onSuccessCheckShopDomain(boolean existed);

    void onErrorCheckShopDomain(Throwable t);

}