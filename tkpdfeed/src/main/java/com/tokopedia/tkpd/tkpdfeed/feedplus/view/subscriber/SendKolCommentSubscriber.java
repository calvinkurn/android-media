package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.SendKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class SendKolCommentSubscriber extends Subscriber<SendKolCommentDomain> {
    private final KolComment.View viewListener;

    public SendKolCommentSubscriber(KolComment.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissProgressDialog();
        viewListener.onErrorSendComment(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendKolCommentDomain sendKolCommentDomain) {
        viewListener.dismissProgressDialog();
        viewListener.onSuccessSendComment();
    }
}
