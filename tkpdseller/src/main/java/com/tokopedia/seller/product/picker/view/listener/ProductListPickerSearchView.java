package com.tokopedia.seller.product.picker.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.picker.view.model.ProductListSellerModelView;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public interface ProductListPickerSearchView extends CustomerView {
    void onErrorGetProductList(Throwable e);

    void onSuccessGetProductList(ProductListSellerModelView transform);
}
