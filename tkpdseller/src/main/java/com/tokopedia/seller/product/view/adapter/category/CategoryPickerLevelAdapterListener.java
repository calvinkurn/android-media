package com.tokopedia.seller.product.view.adapter.category;

import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/6/17.
 */

public interface CategoryPickerLevelAdapterListener {
    void selectParent(int categoryId);

    void selectSetCategory(List<CategoryViewModel> listCategory);
}
