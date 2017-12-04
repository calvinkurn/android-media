package com.tokopedia.seller.product.draft.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by User on 6/21/2017.
 */

public interface ProductDraftSaveBulkView extends CustomerView {
    void onSaveBulkDraftSuccess(List<Long> productIds);
    void onErrorSaveBulkDraft(Throwable throwable);
    void hideDraftLoading();
}
