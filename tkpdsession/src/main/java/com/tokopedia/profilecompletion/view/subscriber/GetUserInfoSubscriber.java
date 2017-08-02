package com.tokopedia.profilecompletion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.profilecompletion.view.listener.GetProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel;
import com.tokopedia.session.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoSubscriber extends Subscriber<GetUserInfoDomainModel> {

    private ProfileCompletionContract.View listener;

    public GetUserInfoSubscriber(ProfileCompletionContract.View view) {
        this.listener = view;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("NISNIS error" + e.toString());
        if (e instanceof UnknownHostException) {
            listener.onErrorGetUserInfo(listener.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            listener.onErrorGetUserInfo(listener.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            listener.onErrorGetUserInfo(listener.getString(R.string.default_request_error_internal_server));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    listener.onErrorGetUserInfo(
                            listener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            listener.onErrorGetUserInfo(e.getLocalizedMessage());
        } else {
            listener.onErrorGetUserInfo(listener.getString(R.string.default_request_error_unknown));
        }

    }

    @Override
    public void onNext(GetUserInfoDomainModel getUserInfoDomainModel) {
        CommonUtils.dumper("NISNIS sukses");
        listener.onGetUserInfo(mappingToViewModel(getUserInfoDomainModel.getGetUserInfoDomainData()));
    }

    private ProfileCompletionViewModel mappingToViewModel(GetUserInfoDomainData getUserInfoDomainData) {
        return new ProfileCompletionViewModel(
                getUserInfoDomainData.getGender(),
                getUserInfoDomainData.getPhone(),
                getUserInfoDomainData.getBday(),
                getUserInfoDomainData.getCompletion(),
                getUserInfoDomainData.isPhoneVerified());
    }
}
