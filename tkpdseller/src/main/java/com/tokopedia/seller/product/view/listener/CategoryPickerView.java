package com.tokopedia.seller.product.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.CategoryLevelViewModel;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public interface CategoryPickerView extends CustomerView {
    void showLoadingDialog();

    void dismissLoadingDialog();

    void renderCategory(List<CategoryViewModel> map);

    void renderCategoryFromSelected(List<CategoryLevelViewModel> categoryLevelDomainModels);

    void showRetryEmpty();
}
