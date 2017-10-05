package com.tokopedia.seller.product.draft.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by User on 6/21/2017.
 */

public interface ProductDraftListCountView extends CustomerView {
    void onDraftCountLoaded(long rowCount);
    void onDraftCountLoadError();
}
