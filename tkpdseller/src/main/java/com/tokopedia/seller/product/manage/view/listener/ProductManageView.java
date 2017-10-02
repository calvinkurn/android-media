package com.tokopedia.seller.product.manage.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManageView extends CustomerView {
    void onErrorEditPrice();

    void onSuccessEditPrice();

    void onSuccessDeleteProduct();

    void onErrorDeleteProduct();

    void onGetProductList(ProductListManageModelView transform);

    void onErrorGetProductList(Throwable e);

    void showLoadingProgress();

    void hideLoadingProgress();

    void onGetFeaturedProductList(List<String> data);

    void onErrorGetFeaturedProductList();

    void onErrorSetCashback();

    void onSuccessSetCashback();
}
