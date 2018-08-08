package com.tokopedia.profilecompletion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;
import com.tokopedia.profilecompletion.view.listener.EditProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.session.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author by nisie on 7/3/17.
 */

public class EditUserInfoSubscriber extends rx.Subscriber<EditUserInfoDomainModel> {

    private final ProfileCompletionContract.View listener;
    private int edit;

    public EditUserInfoSubscriber(ProfileCompletionContract.View view, int edit) {
        this.listener = view;
        this.edit = edit;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("NISNIS error" + e.toString());
        if (e instanceof UnknownHostException) {
            listener.onFailedEditProfile(listener.getString(R.string.msg_no_connection));
        } else if (e instanceof SocketTimeoutException) {
            listener.onFailedEditProfile(listener.getString(R.string.default_request_error_timeout));
        } else if (e instanceof IOException) {
            listener.onFailedEditProfile(listener.getString(R.string.default_request_error_internal_server));
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                e.getLocalizedMessage().length() <= 3) {
            new ErrorHandler(new ErrorListener() {
                @Override
                public void onUnknown() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_unknown));
                }

                @Override
                public void onTimeout() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_timeout));
                }

                @Override
                public void onServerError() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_internal_server));
                }

                @Override
                public void onBadRequest() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_bad_request));
                }

                @Override
                public void onForbidden() {
                    listener.onFailedEditProfile(
                            listener.getString(R.string.default_request_error_forbidden_auth));
                }
            }, Integer.parseInt(e.getLocalizedMessage()));
        } else if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            listener.onFailedEditProfile(e.getLocalizedMessage());
        } else {
            listener.onFailedEditProfile(listener.getString(R.string.default_request_error_unknown));
        }
        CommonUtils.dumper("NISNIS error" + e.toString());
    }

    @Override
    public void onNext(EditUserInfoDomainModel editUserInfoDomainModel) {
        CommonUtils.dumper("NISNIS sukses");
        if(editUserInfoDomainModel.isSuccess()) {
            listener.onSuccessEditProfile(edit);
        }else {
            listener.onFailedEditProfile(editUserInfoDomainModel.getErrorMessage());
        }
    }
}
