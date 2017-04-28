package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.view.listener.ProductAddView;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

/**
 * @author sebastianuskh on 4/26/17.
 */

public interface ProductDraftView extends ProductPopulateView {

    void onSuccessLoadProduct(UploadProductInputViewModel model);

    void onErrorLoadProduct(String errorMessage);

}
