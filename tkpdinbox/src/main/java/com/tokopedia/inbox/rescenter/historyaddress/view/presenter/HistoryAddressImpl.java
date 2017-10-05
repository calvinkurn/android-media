package com.tokopedia.inbox.rescenter.historyaddress.view.presenter;

import com.tokopedia.inbox.rescenter.historyaddress.HistoryAddressFragment;
import com.tokopedia.inbox.rescenter.historyaddress.view.listener.HistoryAddress;
import com.tokopedia.inbox.rescenter.historyaddress.view.listener.HistoryAddressViewListener;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAddressImpl implements HistoryAddress {
    private final HistoryAddressViewListener viewListener;

    public HistoryAddressImpl(HistoryAddressViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void generateFragment() {
        viewListener.setHistoryShippingFragment(HistoryAddressFragment.createInstance(generateExtras()));
    }

    private String generateExtras() {
        return viewListener.getResolutionID();
    }
}
