package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {
    void onSuccessAddProduct();

    void createNotification(String productDraftId, String productName);

    void notificationUpdate(String productDraftId);

    void notificationComplete(String productDraftId);

    void sendSuccessBroadcast(AddProductDomainModel addProductDomainModel);

    void onFailedAddProduct();

    void notificationFailed(String errorMessage, String productDraftId, @ProductStatus int productStatus);

    void sendFailedBroadcast(String errorMessage);
}
