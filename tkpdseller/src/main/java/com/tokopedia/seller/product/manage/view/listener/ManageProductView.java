package com.tokopedia.seller.product.manage.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ManageProductView extends CustomerView {
    void onErrorEditPrice();

    void onSuccessEditPrice();

    void onSuccessDeleteProduct();

    void onErrorDeleteProduct();

    void onGetProductList(ProductListManageModelView transform);

    void onErrorGetProductList(Throwable e);
}
