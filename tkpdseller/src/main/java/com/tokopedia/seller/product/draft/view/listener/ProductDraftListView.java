package com.tokopedia.seller.product.draft.view.listener;

import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;

import java.util.List;

/**
 * Created by User on 8/10/2017.
 */

public interface ProductDraftListView extends BaseListViewListener<ProductDraftViewModel> {
    void onSaveBulkDraftSuccess(List<Long> productIds);
    void onErrorSaveBulkDraft(Throwable throwable);
    void onSuccessDeleteAllDraft();
    void onErrorDeleteAllDraft();
    void hideDraftLoading();
}
