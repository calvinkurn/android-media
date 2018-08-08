package com.tokopedia.seller.product.edit.view.listener;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public interface CategoryPickerFragmentListener {
    void selectSetCategory(List<CategoryViewModel> listCategory);
}
