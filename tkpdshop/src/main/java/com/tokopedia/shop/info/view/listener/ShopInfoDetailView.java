package com.tokopedia.shop.info.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;

public interface ShopInfoDetailView extends CustomerView {

    void onSuccessGetShopInfo(ShopInfo shopInfo);

    void onErrorGetShopInfo(Throwable e);
}
