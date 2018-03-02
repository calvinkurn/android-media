package com.tokopedia.shop.product.view.listener;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;

public interface ShopProductListLimitedView extends BaseListViewListener<ShopProductBaseViewModel> {

    void onErrorRemoveFromWishList(Throwable e);

    void onSuccessRemoveFromWishList(String productId, Boolean value);

    void onErrorAddToWishList(Throwable e);

    void onSuccessAddToWishList(String productId, Boolean value);

    void showLoading();

    void hideLoading();
}
