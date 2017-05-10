package com.tokopedia.session.activation.view.subscriber;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.data.ChangeEmailModel;
import com.tokopedia.session.activation.view.viewListener.ChangeEmailView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailSubscriber extends Subscriber<ChangeEmailModel> {
    private final ChangeEmailView viewListener;

    public ChangeEmailSubscriber(ChangeEmailView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {
        
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorChangeEmail(viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            viewListener.onErrorChangeEmail(viewListener.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            viewListener.onErrorChangeEmail(viewListener.getString(R.string.default_request_error_internal_server));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorChangeEmail(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorChangeEmail(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorChangeEmail(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorChangeEmail(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorChangeEmail(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorChangeEmail(e.getLocalizedMessage());
        } else {
            viewListener.onErrorChangeEmail(viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(ChangeEmailModel changeEmailModel) {
        if(changeEmailModel.isSuccess()){
            viewListener.onSuccessChangeEmail();
        }else{
            viewListener.onErrorChangeEmail(changeEmailModel.getErrorMessage());
        }
    }
}
