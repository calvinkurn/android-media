package com.tokopedia.inbox.rescenter.shipping.presenter;

import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingPresenter;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingView;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingImpl implements InputShippingPresenter {

    private final InputShippingView view;

    public InputShippingImpl(InputShippingView view) {
        this.view = view;
    }

}
