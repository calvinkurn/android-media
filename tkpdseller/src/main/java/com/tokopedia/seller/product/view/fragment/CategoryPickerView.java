package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public interface CategoryPickerView extends CustomerView {
    void showLoadingDialog();

    void dismissLoadingDialog();

    void renderCategory(List<CategoryViewModel> map);
}
