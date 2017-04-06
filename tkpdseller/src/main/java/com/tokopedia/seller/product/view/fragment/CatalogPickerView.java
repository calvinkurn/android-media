package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;

import java.util.List;

/**
 * @author hendry on 4/4/17.
 */

public interface CatalogPickerView extends CustomerView {
    void showError(Throwable e);

    void successFetchData(List<Catalog> catalogViewModelList, int maxRows);
}
