package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author sebastianuskh on 4/4/17.
 */

public interface CatalogPickerView extends CustomerView {
    void showLoadingDialog();

    void dismissLoadingDialog();
}
