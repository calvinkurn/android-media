package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

/**
 * @author sebastianuskh on 4/20/17.
 */

public interface AddProductServiceListener extends CustomerView {
    void onSuccessAddProduct();

    void notificationComplete(long draftProductId);

    void sendSuccessBroadcast(ProductViewModel productViewModel);

    void onFailedAddProduct();

    void notificationFailed(Throwable error, long draftProductId, @ProductStatus int productStatus);

    void sendFailedBroadcast(Throwable error);
}
