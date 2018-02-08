package com.tokopedia.seller.product.category.view.adapter;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/6/17.
 */

public interface CategoryPickerLevelAdapterListener {
    void selectParent(long categoryId);

    void selectSetCategory(List<CategoryViewModel> listCategory);
}
