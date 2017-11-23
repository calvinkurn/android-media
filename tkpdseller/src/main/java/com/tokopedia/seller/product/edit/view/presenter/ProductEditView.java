package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface ProductEditView extends ProductDraftView {

    void onErrorFetchEditProduct(Throwable throwable);
}
