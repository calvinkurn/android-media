package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.listener.ProductDraftListView;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftListPresenter extends BaseDaggerPresenter<ProductDraftListView> {
    public abstract void fetchAllDraftData();
    public abstract void deleteProductDraft(long draftId);
}
