package com.tokopedia.seller.manageitem.view.presenter;


import com.tokopedia.seller.manageitem.common.listener.ProductAddView;
import com.tokopedia.seller.manageitem.common.util.AddEditPageType;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddPresenter<T extends ProductAddView> {
    void getShopInfo(AddEditPageType addEditPageType);
}
