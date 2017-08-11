package com.tokopedia.seller.product.draft.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;

import java.util.List;

/**
 * Created by User on 8/10/2017.
 */

public interface ProductDraftListView extends BaseListViewListener<ProductDraftViewModel> {
    void onSaveBulkDraftSuccess(List<Long> productIds);
    void onSaveBulkDraftError(Throwable throwable);
    void onSaveInstagramResolutionError(int position, String localPath);
}
