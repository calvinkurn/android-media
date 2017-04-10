package com.tokopedia.seller.product.view.adapter.category;

/**
 * @author sebastianuskh on 4/6/17.
 */

public interface CategoryPickerAdapterListener {
    void selectParent(int categoryId);

    void unselectParent(int level);

    void selectCategoryItem();
}
