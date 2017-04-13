package com.tokopedia.session.register.subscriber;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.session.R;
import com.tokopedia.session.register.data.RegisterEmailModel;
import com.tokopedia.session.register.data.pojo.RegisterEmailData;
import com.tokopedia.session.register.viewmodel.RegisterEmailViewModel;
import com.tokopedia.session.register.viewlistener.RegisterEmailViewListener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailSubscriber extends Subscriber<RegisterEmailModel> {

    private final RegisterEmailViewListener viewListener;

    public RegisterEmailSubscriber(RegisterEmailViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorRegister(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorRegister(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorRegister(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorRegister(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorRegister(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorRegister(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException &&
                e.getLocalizedMessage() != null) {
            viewListener.onErrorRegister(e.getLocalizedMessage());
        } else {
            viewListener.onErrorRegister(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(RegisterEmailModel registerEmailModel) {
        viewListener.onSuccessRegister(mappingToViewModel(registerEmailModel.getRegisterEmailData()));
    }

    private RegisterEmailViewModel mappingToViewModel(RegisterEmailData data) {
        RegisterEmailViewModel viewModel = new RegisterEmailViewModel();
        viewModel.setAction(data.getAction());
        viewModel.setIsActive(data.getIsActive());
        viewModel.setIsSuccess(data.getIsSuccess());
        viewModel.setUserId(data.getuId());
        return viewModel;
    }
}
