package com.tokopedia.seller.product.picker.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerSearchView;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public interface ProductListPickerSearchPresenter extends CustomerPresenter<ProductListPickerSearchView> {
    void getProductList(int page, String keywordFilter);
}
