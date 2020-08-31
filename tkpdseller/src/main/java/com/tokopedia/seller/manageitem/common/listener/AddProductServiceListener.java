package com.tokopedia.seller.manageitem.common.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {

    void onSuccessAddProduct(ProductSubmitNotificationListener notificationCountListener);

    void onFailedAddProduct(Throwable t, ProductSubmitNotificationListener notificationCountListener);

}