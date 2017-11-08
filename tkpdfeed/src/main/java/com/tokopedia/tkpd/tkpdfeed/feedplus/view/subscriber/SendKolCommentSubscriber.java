package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
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
        if (sendKolCommentDomain != null && !TextUtils.isEmpty(sendKolCommentDomain.getComment()))
            viewListener.onSuccessSendComment(sendKolCommentDomain);
        else {
            viewListener.onErrorSendComment(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));
        }
    }
}
