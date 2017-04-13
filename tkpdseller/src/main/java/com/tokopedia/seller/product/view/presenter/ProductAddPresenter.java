package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.fragment.ProductAddView;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public abstract class ProductAddPresenter extends BaseDaggerPresenter<ProductAddView>{
    public abstract void saveDraft(UploadProductInputViewModel viewModel);
}
