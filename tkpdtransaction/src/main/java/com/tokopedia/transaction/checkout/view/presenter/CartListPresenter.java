package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.view.ICartListView;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListPresenter implements ICartListPresenter {
    private final ICartListView view;

    @Inject
    public CartListPresenter(ICartListView view) {
        this.view = view;
    }

    @Override
    public void processGetCartData() {

    }
}
