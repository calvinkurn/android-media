package com.tokopedia.otp.phoneverification.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.domain.pojo.VerifyPhoneNumberPojo;
import com.tokopedia.session.R;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyPhoneNumberMapper implements Func1<Response<TkpdResponse>, VerifyPhoneNumberDomain> {

    @Inject
    public VerifyPhoneNumberMapper() {
    }

    @Override
    public VerifyPhoneNumberDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                VerifyPhoneNumberPojo pojo = response.body().convertDataObj(VerifyPhoneNumberPojo.class);
                return mappingToViewModel(pojo, response.body().getStatusMessageJoined());
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private VerifyPhoneNumberDomain mappingToViewModel(VerifyPhoneNumberPojo pojo, String statusMessageJoined) {
        return new VerifyPhoneNumberDomain(pojo.isSuccess(), statusMessageJoined);
    }

}