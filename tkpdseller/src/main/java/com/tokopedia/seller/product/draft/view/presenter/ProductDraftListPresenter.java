package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListView;
import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;

import java.util.ArrayList;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftListPresenter extends BaseDaggerPresenter<ProductDraftListView> {

    public abstract void fetchAllDraftDataWithUpdateUploading();
    public abstract void fetchAllDraftData();
    public abstract void deleteProductDraft(long draftId);
    public abstract void clearAllDraftData();
}