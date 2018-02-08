package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftSaveBulkView;

import java.util.ArrayList;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftSaveBulkPresenter extends BaseDaggerPresenter<ProductDraftSaveBulkView> {
    public abstract void saveInstagramToDraft(Context context, ArrayList<String>localPathList, ArrayList<String> instagramDescList);
}
