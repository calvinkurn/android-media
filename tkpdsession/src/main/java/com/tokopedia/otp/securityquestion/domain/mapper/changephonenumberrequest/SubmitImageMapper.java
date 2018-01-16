package com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.SubmitImageModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/13/17.
 */

public class SubmitImageMapper implements Func1<Response<TkpdResponse>, SubmitImageModel> {

    @Override
    public SubmitImageModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private SubmitImageModel mappingResponse(Response<TkpdResponse> response) {
        SubmitImageModel model = new SubmitImageModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                model.setSuccess(true);
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