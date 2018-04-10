package com.tokopedia.otp.registerphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.otp.registerphonenumber.data.pojo.verifyotp.VerifyOtpResponse;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.VerifyOtpViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 5/3/18.
 */

public class VerifyOtpMapper implements Func1<Response<TkpdResponse>, VerifyOtpViewModel> {

    @Inject
    public VerifyOtpMapper() {
    }

    @Override
    public VerifyOtpViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().getErrorMessages() != null && !response.body().getErrorMessages().isEmpty()) {
                VerifyOtpResponse validateOtpSQData = response.body().convertDataObj(
                        VerifyOtpResponse.class);
                return convertToDomain(validateOtpSQData.isSuccess(), validateOtpSQData.getUuid());
            } else if (response.body().isNullData()) {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            } else {
                throw new ErrorMessageException("");
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

    private boolean responseIsSecurityQuestion(TkpdResponse body) {
        return body.toString().contains("uuid");
    }
    private VerifyOtpViewModel convertToDomain(boolean isSuccess, String uuid) {
        return new VerifyOtpViewModel(isSuccess, uuid);
    }
}
