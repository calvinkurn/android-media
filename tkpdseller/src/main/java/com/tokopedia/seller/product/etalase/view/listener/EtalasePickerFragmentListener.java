package com.tokopedia.seller.product.etalase.view.listener;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface EtalasePickerFragmentListener {
    void openAddNewEtalaseDialog();

    void selectEtalase(Integer etalaseId, String etalaseName);
}
