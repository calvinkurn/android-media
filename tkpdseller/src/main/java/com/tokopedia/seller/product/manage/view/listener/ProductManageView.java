package com.tokopedia.seller.product.manage.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManageView extends BaseListViewListener<ProductManageViewModel> {

    void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNext);

    void onErrorEditPrice();

    void onSuccessEditPrice();

    void onSuccessDeleteProduct();

    void onErrorDeleteProduct();

    void showLoadingProgress();

    void hideLoadingProgress();

    void onGetFeaturedProductList(List<String> data);

    void onErrorGetFeaturedProductList();
}
