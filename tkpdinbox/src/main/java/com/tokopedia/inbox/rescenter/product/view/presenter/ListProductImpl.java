package com.tokopedia.inbox.rescenter.product.view.presenter;

import com.tokopedia.inbox.rescenter.product.ListProductFragment;
import com.tokopedia.inbox.rescenter.product.view.listener.ListProduct;
import com.tokopedia.inbox.rescenter.product.view.listener.ListProductViewListener;

/**
 * Created by hangnadi on 3/23/17.
 */

public class ListProductImpl implements ListProduct {
    private final ListProductViewListener viewListener;

    public ListProductImpl(ListProductViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void generateFragment() {
        viewListener.setHistoryShippingFragment(ListProductFragment.createInstance(generateExtras()));
    }

    private String generateExtras() {
        return viewListener.getResolutionID();
    }
}
