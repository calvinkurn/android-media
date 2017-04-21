package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {
    void onSuccessAddProduct();

    void createNotification(String productName);

    void notificationUpdate(int stepNotification);

    void notificationComplete();
}
