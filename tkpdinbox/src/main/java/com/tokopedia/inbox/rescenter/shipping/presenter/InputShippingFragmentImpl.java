package com.tokopedia.inbox.rescenter.shipping.presenter;

import com.tokopedia.inbox.rescenter.shipping.view.InputShippingRefNumView;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingFragmentImpl implements InputShippingFragmentPresenter {

    private final InputShippingRefNumView viewListener;

    public InputShippingFragmentImpl(InputShippingRefNumView viewListener) {
        this.viewListener = viewListener;
    }
}
