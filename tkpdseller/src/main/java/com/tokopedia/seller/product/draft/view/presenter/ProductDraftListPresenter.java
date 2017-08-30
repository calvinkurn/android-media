package com.tokopedia.seller.product.draft.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftListPresenter extends BaseDaggerPresenter<BaseListViewListener<ProductDraftViewModel>> {

    public abstract void fetchAllDraftDataWithUpdateUploading();
    public abstract void fetchAllDraftData();
    public abstract void deleteProductDraft(long draftId);
}