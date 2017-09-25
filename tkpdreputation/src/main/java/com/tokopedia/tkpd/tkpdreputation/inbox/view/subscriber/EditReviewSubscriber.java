package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;

import rx.Subscriber;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewSubscriber extends Subscriber<SendReviewDomain> {
    private final InboxReputationForm.View viewListener;

    public EditReviewSubscriber(InboxReputationForm.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingProgress();
        viewListener.onErrorEditReview(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendReviewDomain sendReviewDomain) {
        viewListener.finishLoadingProgress();
        if (sendReviewDomain.isSuccess())
            viewListener.onSuccessEditReview();
        else
            viewListener.onErrorSendReview(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
    }
}
