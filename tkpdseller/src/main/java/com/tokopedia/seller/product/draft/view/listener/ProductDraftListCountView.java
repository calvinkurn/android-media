package com.tokopedia.seller.product.draft.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by User on 6/21/2017.
 */

public interface ProductDraftListCountView extends CustomerView {
    void onDraftCountLoaded(long rowCount);
    void onDraftCountLoadError();
    void onSaveBulkDraftSuccess(List<Long> productIds);
    void onSaveBulkDraftError(Throwable throwable);
    void onSaveInstagramResolutionError(int position, String localPath);
}
