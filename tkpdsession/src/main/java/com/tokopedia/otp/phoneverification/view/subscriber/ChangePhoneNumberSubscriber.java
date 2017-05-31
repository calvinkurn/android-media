package com.tokopedia.otp.phoneverification.view.subscriber;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.otp.phoneverification.data.ChangePhoneNumberModel;
import com.tokopedia.otp.phoneverification.view.listener.ChangePhoneNumberView;
import com.tokopedia.session.R;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberSubscriber extends Subscriber<ChangePhoneNumberModel> {

    private final ChangePhoneNumberView viewListener;

    public ChangePhoneNumberSubscriber(ChangePhoneNumberView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            viewListener.onErrorChangePhoneNumber(
                    viewListener.getString(R.string.msg_no_connection));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    viewListener.onErrorChangePhoneNumber(
                            viewListener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    viewListener.onErrorChangePhoneNumber(
                            viewListener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    viewListener.onErrorChangePhoneNumber(
                            viewListener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    viewListener.onErrorChangePhoneNumber(
                            viewListener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    viewListener.onErrorChangePhoneNumber(
                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorChangePhoneNumber(e.getLocalizedMessage());
        } else {
            viewListener.onErrorChangePhoneNumber(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onNext(ChangePhoneNumberModel changePhoneNumberModel) {
        if(changePhoneNumberModel.isSuccess())
            viewListener.onSuccessChangePhoneNumber();
        else
            viewListener.onErrorChangePhoneNumber(changePhoneNumberModel.getErrorMessage());
    }
}
