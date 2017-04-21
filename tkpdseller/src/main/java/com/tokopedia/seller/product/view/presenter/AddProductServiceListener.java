package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {
    void onSuccessAddProduct();

    void createNotification(String productName);

    void notificationUpdate(int stepNotification);

    void notificationComplete();

    void sendSuccessBroadcast(AddProductDomainModel addProductDomainModel);

    void onFailedAddProduct();

    void notificationFailed(String errorMessage);

    void sendFailedBroadcast(String errorMessage);
}
