package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.view.listener.ProductAddView;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface ProductEditView extends ProductAddView {
    void populateView(UploadProductInputViewModel model);
}
