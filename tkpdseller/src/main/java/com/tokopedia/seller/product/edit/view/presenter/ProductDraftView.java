package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.seller.product.edit.view.listener.ProductAddView;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

/**
 * @author sebastianuskh on 4/26/17.
 */

public interface ProductDraftView extends ProductAddView {

    void onSuccessLoadDraftProduct(ProductViewModel model);

    void onErrorLoadDraftProduct(Throwable throwable);

}
