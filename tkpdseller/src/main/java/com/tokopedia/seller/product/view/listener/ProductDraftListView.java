package com.tokopedia.seller.product.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/21/2017.
 */

public interface ProductDraftListView extends CustomerView {
    void onLoadSearchError();

    void hideLoading();

    void onSearchLoaded(List<ProductDraftViewModel> listDraft);
}
