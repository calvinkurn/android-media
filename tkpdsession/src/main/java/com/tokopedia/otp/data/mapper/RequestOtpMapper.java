package com.tokopedia.otp.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.otp.RequestOtpData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.data.model.RequestOtpModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 * @deprecated use RequestOtpMapper in tkpdsession instead.
 */

@Deprecated
public class RequestOtpMapper implements Func1<Response<TkpdResponse>, RequestOtpModel> {
    @Override
    public RequestOtpModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private RequestOtpModel mappingResponse(Response<TkpdResponse> response) {
        RequestOtpModel model = new RequestOtpModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                RequestOtpData data = response.body().convertDataObj(RequestOtpData.class);
                model.setSuccess(true);
                model.setRequestOtpData(data);
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
