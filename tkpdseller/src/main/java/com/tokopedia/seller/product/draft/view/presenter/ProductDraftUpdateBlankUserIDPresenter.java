package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListView;

import java.util.ArrayList;

/**
 * Created by User on 6/20/2017.
 */

public abstract class ProductDraftUpdateBlankUserIDPresenter extends BaseDaggerPresenter<CustomerView> {

    public abstract void updateBlankUserId();
}