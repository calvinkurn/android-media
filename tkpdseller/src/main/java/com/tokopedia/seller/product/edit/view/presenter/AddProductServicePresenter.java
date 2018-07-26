package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.edit.domain.listener.ProductSubmitNotificationListener;

/**
 * @author sebastianuskh on 4/20/17.
 */

public abstract class AddProductServicePresenter extends BaseDaggerPresenter<AddProductServiceListener>{

    public abstract void uploadProduct(long draftProductId, ProductSubmitNotificationListener notificationCountListener);
}
