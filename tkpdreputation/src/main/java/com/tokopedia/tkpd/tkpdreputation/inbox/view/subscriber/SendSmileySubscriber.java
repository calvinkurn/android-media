package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileySubscriber extends Subscriber<SendSmileyReputationDomain> {


    private final InboxReputationDetail.View viewListener;

    public SendSmileySubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorSendSmiley(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendSmileyReputationDomain sendSmileyReputationDomain) {
        viewListener.finishLoadingDialog();
        viewListener.onSuccessSendSmiley();

    }
}
