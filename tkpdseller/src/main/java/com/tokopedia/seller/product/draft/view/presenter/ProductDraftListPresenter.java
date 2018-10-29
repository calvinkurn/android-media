package com.tokopedia.seller.product.draft.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListView;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftListPresenter extends BaseDaggerPresenter<ProductDraftListView> {

    public abstract void fetchAllDraftDataWithUpdateUploading();
    public abstract void fetchAllDraftData();
    public abstract void deleteProductDraft(long draftProductId);
    public abstract void clearAllDraftData();
}