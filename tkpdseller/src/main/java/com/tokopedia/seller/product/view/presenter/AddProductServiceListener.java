package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

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

    void notificationFailed(Throwable error, String productDraftId, @ProductStatus int productStatus);

    void sendFailedBroadcast(Throwable error);
}
