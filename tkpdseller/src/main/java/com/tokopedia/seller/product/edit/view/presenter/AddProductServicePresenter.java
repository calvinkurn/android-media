package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

/**
 * @author sebastianuskh on 4/20/17.
 */

public abstract class AddProductServicePresenter extends BaseDaggerPresenter<AddProductServiceListener>{

    public abstract void uploadProduct(long draftProductId, boolean isAdd);
}
