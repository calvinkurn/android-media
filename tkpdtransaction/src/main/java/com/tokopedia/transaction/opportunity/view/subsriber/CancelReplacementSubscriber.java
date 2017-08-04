package com.tokopedia.transaction.opportunity.view.subsriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;
import com.tokopedia.transaction.purchase.listener.TxListViewListener;

import rx.Subscriber;

/**
 * @author by nisie on 6/2/17.
 */

public class CancelReplacementSubscriber extends Subscriber<CancelReplacementModel> {

    private final TxListViewListener viewListener;

    public CancelReplacementSubscriber(TxListViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorCancelReplacement(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(CancelReplacementModel cancelReplacementModel) {
        if (cancelReplacementModel.isSuccess())
            viewListener.onSuccessCancelReplacement();
        else
            viewListener.onErrorCancelReplacement(
                    viewListener.getStringFromResource(R.string.default_request_error_unknown));
    }
}
