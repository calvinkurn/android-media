package com.tokopedia.seller.gmsubscribe.view.product.fragment;

/**
 * Created by sebastianuskh on 1/23/17.
 */

public interface GMProductFragmentListener {

    void changeActionBarTitle(String title);

    void setDrawer(boolean isShown);

    void finishProductSelection(int productId, int returnType);
}
