package com.tokopedia.seller.product.edit.view.adapter.category;

/**
 * @author sebastianuskh on 4/6/17.
 */

public interface CategoryPickerAdapterListener {
    void selectParent(long categoryId);

    void unselectParent(int level);

    void selectCategoryItem();
}
