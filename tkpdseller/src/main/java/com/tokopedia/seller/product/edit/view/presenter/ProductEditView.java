package com.tokopedia.seller.product.edit.view.presenter;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface ProductEditView extends ProductDraftView {

    void onErrorFetchEditProduct(Throwable throwable);
}
