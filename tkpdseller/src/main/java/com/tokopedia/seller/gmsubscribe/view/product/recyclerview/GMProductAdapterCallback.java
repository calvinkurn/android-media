package com.tokopedia.seller.gmsubscribe.view.product.recyclerview;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GMProductAdapterCallback {

    void selectedProductId(Integer gmProductViewModel);
    Integer getSelectedProductId();
}
