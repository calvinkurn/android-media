package com.tokopedia.session.activation.view.subscriber;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.data.ResendActivationModel;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationSubscriber extends Subscriber<ResendActivationModel> {

    private final RegisterActivationView viewListener;

    public ResendActivationSubscriber(RegisterActivationView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorResendActivation(viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_internal_server));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorResendActivation(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorResendActivation(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorResendActivation(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorResendActivation(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorResendActivation(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorResendActivation(e.getLocalizedMessage());
        } else {
            viewListener.onErrorResendActivation(viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(ResendActivationModel resendActivationModel) {
        if (resendActivationModel.isSuccess())
            viewListener.onSuccessResendActivation(resendActivationModel.getStatusMessage());
        else {
            viewListener.onErrorResendActivation(resendActivationModel.getErrorMessage());
        }
    }
}
