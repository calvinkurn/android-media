package com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest;

import com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest.ValidateImageData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.ValidateImageModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/9/17.
 */

public class ValidateImageMapper implements Func1<Response<TkpdResponse>, ValidateImageModel> {


    @Override
    public ValidateImageModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ValidateImageModel mappingResponse(Response<TkpdResponse> response) {
        ValidateImageModel model = new ValidateImageModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ValidateImageData data = response.body().convertDataObj(ValidateImageData.class);
                model.setSuccess(true);
                model.setValidateImageData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    model.setSuccess(false);
                    model.setErrorMessage(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
        } else {
            model.setSuccess(false);
        }
        model.setResponseCode(response.code());
        return model;
    }
}
