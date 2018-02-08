package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView;

import java.util.ArrayList;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftListCountPresenter extends BaseDaggerPresenter<ProductDraftListCountView> {
    public abstract void fetchAllDraftCountWithUpdateUploading();
    public abstract void fetchAllDraftCount();
    public abstract void clearAllDraft();
}
