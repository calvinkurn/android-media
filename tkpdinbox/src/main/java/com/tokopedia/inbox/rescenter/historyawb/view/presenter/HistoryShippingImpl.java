package com.tokopedia.inbox.rescenter.historyawb.view.presenter;

import com.tokopedia.inbox.rescenter.historyawb.HistoryShippingFragment;
import com.tokopedia.inbox.rescenter.historyawb.view.listener.HistoryShipping;
import com.tokopedia.inbox.rescenter.historyawb.view.listener.HistoryShippingViewListener;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryShippingImpl implements HistoryShipping {
    private final HistoryShippingViewListener viewListener;

    public HistoryShippingImpl(HistoryShippingViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void generateFragment() {
        viewListener.setHistoryShippingFragment(HistoryShippingFragment.createInstance(generateExtras()));
    }

    private String generateExtras() {
        return viewListener.getResolutionID();
    }
}
