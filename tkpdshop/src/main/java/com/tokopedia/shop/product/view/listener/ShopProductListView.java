package com.tokopedia.shop.product.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

public interface ShopProductListView extends BaseListViewListener<ShopProductViewModel> {

    void onSuccessAddToWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessGetShopName(String shopName);

    void onSuccessGetEtalase(String etalaseId, String etalaseName);

}
