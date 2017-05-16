package com.tokopedia.seller.gmsubscribe.view.recyclerview;

/**
 * Created by sebastianuskh on 1/27/17.
 */

public interface GmProductAdapterCallback {

    void selectedProductId(Integer gmProductViewModel);

    Integer getSelectedProductId();
}
