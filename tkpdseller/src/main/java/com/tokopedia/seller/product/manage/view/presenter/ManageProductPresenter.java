package com.tokopedia.seller.product.manage.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.product.manage.view.listener.ManageProductView;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ManageProductPresenter extends CustomerPresenter<ManageProductView> {
    void editPrice(String productId, String price, String priceCurrency);
    void deleteProduct(String productId);
    void getListProduct();
}
