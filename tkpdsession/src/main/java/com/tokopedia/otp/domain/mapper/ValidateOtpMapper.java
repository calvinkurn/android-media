package com.tokopedia.otp.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.otp.data.model.ValidateOtpDomain;
import com.tokopedia.otp.domain.pojo.ValidateOtpPojo;
import com.tokopedia.otp.domain.pojo.ValidateOtpSQPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpMapper implements Func1<Response<TkpdResponse>, ValidateOtpDomain> {

    @Inject
    public ValidateOtpMapper() {
    }

    @Override
    public ValidateOtpDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                if (responseIsSecurityQuestion(response.body())) {
                    ValidateOtpSQPojo validateOtpSQData = response.body().convertDataObj(
                            ValidateOtpSQPojo.class);
                    return convertToDomain(validateOtpSQData.isSuccess(), validateOtpSQData.getUuid());
                } else {
                    ValidateOtpPojo validateOtpData = response.body().convertDataObj(
                            ValidateOtpPojo.class);
                    return convertToDomain(validateOtpData.isSuccess(), "");
                }
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

    private boolean responseIsSecurityQuestion(TkpdResponse body) {
        return body.toString().contains("uuid");
    }

    private ValidateOtpDomain convertToDomain(boolean isSuccess, String uuid) {
        return new ValidateOtpDomain(isSuccess, uuid);
    }
}
