package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.edit.view.listener.CatalogPickerView;

/**
 * @author hendry on 4/3/17.
 */

public abstract class CatalogPickerPresenter extends BaseDaggerPresenter<CatalogPickerView>{
    public abstract void fetchCatalogData(String keyword, long departmentId, int start, int rows);
}
