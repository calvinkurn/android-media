package com.tokopedia.inbox.rescenter.history;

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
