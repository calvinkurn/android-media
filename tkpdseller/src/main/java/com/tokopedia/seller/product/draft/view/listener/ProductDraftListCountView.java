package com.tokopedia.seller.product.draft.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

import java.util.List;

/**
 * Created by User on 6/21/2017.
 */

public interface ProductDraftListCountView extends CustomerView {
    void onDraftCountLoaded(long rowCount);
    void onDraftCountLoadError();
}
