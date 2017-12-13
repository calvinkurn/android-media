package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/28/17.
 */

public class ReplyReviewSubscriber extends Subscriber<SendReplyReviewDomain> {
    private final InboxReputationDetail.View viewListener;

    public ReplyReviewSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorReplyReview(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendReplyReviewDomain sendReplyReviewDomain) {
        viewListener.finishLoadingDialog();
        if (sendReplyReviewDomain.isSuccess())
            viewListener.onSuccessReplyReview();
        else
            viewListener.onErrorReplyReview(
                    MainApplication.getAppContext().getString(R.string
                            .default_request_error_unknown));

    }
}
