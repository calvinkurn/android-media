package com.tokopedia.session.activation.view.subscriber;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.data.ActivateUnicodeModel;
import com.tokopedia.session.activation.data.pojo.ActivateUnicodeData;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;
import com.tokopedia.session.activation.view.viewmodel.LoginTokenViewModel;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/17/17.
 */

public class ActivateUnicodeSubscriber extends Subscriber<ActivateUnicodeModel> {
    private final RegisterActivationView viewListener;

    public ActivateUnicodeSubscriber(RegisterActivationView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorActivateWithUnicode(viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            viewListener.onErrorActivateWithUnicode(viewListener.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            viewListener.onErrorActivateWithUnicode(viewListener.getString(R.string.default_request_error_internal_server));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorActivateWithUnicode(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorActivateWithUnicode(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorActivateWithUnicode(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorActivateWithUnicode(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorActivateWithUnicode(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorActivateWithUnicode(e.getLocalizedMessage());
        } else {
            viewListener.onErrorActivateWithUnicode(viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(ActivateUnicodeModel activateUnicodeModel) {
        if (activateUnicodeModel.isSuccess())
            viewListener.onSuccessActivateWithUnicode(mappingToViewModel(activateUnicodeModel.getActivateUnicodeData()));
        else {
            viewListener.onErrorActivateWithUnicode(activateUnicodeModel.getErrorMessage());
        }
    }

    private LoginTokenViewModel mappingToViewModel(ActivateUnicodeData activateUnicodeData) {
        LoginTokenViewModel viewModel = new LoginTokenViewModel();
        viewModel.setAccessToken(activateUnicodeData.getAccessToken());
        viewModel.setExpiresIn(activateUnicodeData.getExpiresIn());
        viewModel.setTokenType(activateUnicodeData.getTokenType());
        return viewModel;
    }
}
