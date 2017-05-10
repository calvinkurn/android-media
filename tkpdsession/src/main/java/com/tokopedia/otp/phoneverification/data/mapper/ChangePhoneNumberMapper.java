package com.tokopedia.otp.phoneverification.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.phoneverification.data.ChangePhoneNumberModel;
import com.tokopedia.otp.phoneverification.data.pojo.ChangePhoneNumberData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberMapper implements Func1<Response<TkpdResponse>, ChangePhoneNumberModel> {

    @Override
    public ChangePhoneNumberModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ChangePhoneNumberModel mappingResponse(Response<TkpdResponse> response) {
        ChangePhoneNumberModel model = new ChangePhoneNumberModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ChangePhoneNumberData data = response.body().convertDataObj(ChangePhoneNumberData.class);
                model.setSuccess(true);
                model.setChangePhoneNumberData(data);
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
