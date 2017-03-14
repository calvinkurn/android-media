package com.tokopedia.otp.phoneverification.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.phoneverification.VerifyPhoneNumberData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyPhoneNumberMapper implements Func1<Response<TkpdResponse>, VerifyPhoneNumberModel> {
    @Override
    public VerifyPhoneNumberModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private VerifyPhoneNumberModel mappingResponse(Response<TkpdResponse> response) {
        VerifyPhoneNumberModel model = new VerifyPhoneNumberModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                VerifyPhoneNumberData data = response.body().convertDataObj(VerifyPhoneNumberData.class);
                model.setSuccess(true);
                model.setVerifyPhoneNumberData(data);
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