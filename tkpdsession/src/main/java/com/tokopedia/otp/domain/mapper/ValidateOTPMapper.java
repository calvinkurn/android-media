package com.tokopedia.otp.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.data.model.ValidateOTPDomain;
import com.tokopedia.otp.domain.pojo.ValidateOtpPojo;
import com.tokopedia.session.R;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOTPMapper implements Func1<Response<TkpdResponse>, ValidateOTPDomain> {
    @Override
    public ValidateOTPDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                ValidateOtpPojo validateOtpData = response.body().convertDataObj(
                        ValidateOtpPojo.class);
                return convertToDomain(validateOtpData);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
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

    private ValidateOTPDomain convertToDomain(ValidateOtpPojo validateOtpData) {
        return new ValidateOTPDomain(validateOtpData.isSuccess(), validateOtpData.getUuid());
    }
}
