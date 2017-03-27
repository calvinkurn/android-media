package com.tokopedia.inbox.rescenter.historyaction.view.presenter;

import com.tokopedia.inbox.rescenter.historyaction.HistoryActionFragment;
import com.tokopedia.inbox.rescenter.historyaction.view.listener.HistoryAction;
import com.tokopedia.inbox.rescenter.historyaction.view.listener.HistoryActionViewListener;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryActionImpl implements HistoryAction {
    private final HistoryActionViewListener viewListener;

    public HistoryActionImpl(HistoryActionViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void generateFragment() {
        viewListener.setHistoryShippingFragment(HistoryActionFragment.createInstance(generateExtras()));
    }

    private String generateExtras() {
        return viewListener.getResolutionID();
    }
}
