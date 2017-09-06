package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;

import rx.Subscriber;

/**
 * @author by nisie on 9/5/17.
 */

class SendReviewWithoutImageSubscriber extends Subscriber<SendReviewValidateDomain> {

    private final InboxReputationForm.View viewListener;

    public SendReviewWithoutImageSubscriber(InboxReputationForm.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingProgress();
        viewListener.onErrorSendReview(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendReviewValidateDomain sendReviewValidateDomain) {
        viewListener.finishLoadingProgress();
        if (sendReviewValidateDomain.getIsSuccess() == 1)
            viewListener.onSuccessSendReview();
        else
            viewListener.onErrorSendReview(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
    }
}
