package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.edit.domain.listener.ProductSubmitNotificationListener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {

    void onSuccessAddProduct(ProductSubmitNotificationListener notificationCountListener);

    void onFailedAddProduct(Throwable t, ProductSubmitNotificationListener notificationCountListener);

}