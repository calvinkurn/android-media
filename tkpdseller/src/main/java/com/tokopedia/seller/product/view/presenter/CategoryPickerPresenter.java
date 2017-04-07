package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.fragment.CategoryPickerView;

/**
 * @author sebastianuskh on 4/3/17.
 */

public abstract class CategoryPickerPresenter extends BaseDaggerPresenter<CategoryPickerView>{
    public abstract void fetchCategoryLevelOne();

    public abstract void fetchCategoryWithParent(int categoryId);
}
