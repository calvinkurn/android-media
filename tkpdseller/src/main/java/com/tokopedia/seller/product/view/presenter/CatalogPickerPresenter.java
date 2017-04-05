package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.fragment.CatalogPickerView;
import com.tokopedia.seller.product.view.fragment.CategoryPickerView;

/**
 * @author sebastianuskh on 4/3/17.
 */

public abstract class CatalogPickerPresenter extends BaseDaggerPresenter<CatalogPickerView>{
    public abstract void fetchCatalogData(String keyword, int departmentId,
                                          int start, int rows);
}
