package com.tokopedia.seller.manageitem.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.manageitem.common.listener.AddProductServiceListener;
import com.tokopedia.seller.manageitem.common.listener.ProductSubmitNotificationListener;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;

/**
 * @author sebastianuskh on 4/20/17.
 */

public abstract class AddProductServicePresenter extends BaseDaggerPresenter<AddProductServiceListener>{

    public abstract void uploadProduct(long draftProductId, ProductSubmitNotificationListener notificationCountListener);
    public abstract void uploadProduct(ProductViewModel productViewModel, ProductSubmitNotificationListener notificationCountListener);

}
