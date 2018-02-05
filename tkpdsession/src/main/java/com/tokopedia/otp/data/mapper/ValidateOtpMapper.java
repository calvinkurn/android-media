package com.tokopedia.otp.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.phoneverification.ValidateOtpData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.data.model.ValidateOtpModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 * @deprecated use ValidateOTPMapper instead.
 */
@Deprecated
public class ValidateOtpMapper implements Func1<Response<TkpdResponse>, ValidateOtpModel> {
    @Override
    public ValidateOtpModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ValidateOtpModel mappingResponse(Response<TkpdResponse> response) {
        ValidateOtpModel model = new ValidateOtpModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ValidateOtpData data = response.body().convertDataObj(ValidateOtpData.class);
                model.setSuccess(true);
                model.setValidateOtpData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
