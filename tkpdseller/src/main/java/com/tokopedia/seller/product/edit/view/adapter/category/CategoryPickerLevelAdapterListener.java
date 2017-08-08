package com.tokopedia.seller.product.edit.view.adapter.category;

import com.tokopedia.seller.product.edit.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/6/17.
 */

public interface CategoryPickerLevelAdapterListener {
    void selectParent(long categoryId);

    void selectSetCategory(List<CategoryViewModel> listCategory);
}
