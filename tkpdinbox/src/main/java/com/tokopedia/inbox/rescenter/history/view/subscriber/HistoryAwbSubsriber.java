package com.tokopedia.inbox.rescenter.history.view.subscriber;

import com.tokopedia.inbox.rescenter.history.HistoryShippingFragmentView;
import com.tokopedia.inbox.rescenter.history.domain.model.HistoryAwbData;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAwbSubsriber extends Subscriber<HistoryAwbData> {

    private final HistoryShippingFragmentView fragmentView;

    public HistoryAwbSubsriber(HistoryShippingFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(HistoryAwbData historyAwbData) {
        if (historyAwbData.isSuccess()) {
            fragmentView.setLoadingView(false);
            fragmentView.showInpuNewShippingAwb(true);
        } else {
            fragmentView.setLoadingView(false);
            fragmentView.setErrorMessage(historyAwbData.getMessageError());
        }
    }
}
