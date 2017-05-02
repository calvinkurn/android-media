package com.tokopedia.inbox.rescenter.discussion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionSubscriber extends Subscriber<DiscussionItemViewModel> {

    private ResCenterDiscussionView viewListener;

    public ReplyDiscussionSubscriber(ResCenterDiscussionView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("NISNIS " + e.toString());
        if (e instanceof UnknownHostException) {
            viewListener.onErrorSendReply(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorSendReply(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorSendReply(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorSendReply(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorSendReply(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorSendReply(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof MessageErrorException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorSendReply(e.getLocalizedMessage());
        } else {
            viewListener.onErrorSendReply(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(DiscussionItemViewModel discussionItemViewModel) {
        viewListener.onSuccessSendReply(discussionItemViewModel);
    }
}
