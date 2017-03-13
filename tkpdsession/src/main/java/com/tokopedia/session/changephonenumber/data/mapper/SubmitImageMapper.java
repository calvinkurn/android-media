package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.core.network.entity.changephonenumberrequest.SubmitImageData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.changephonenumber.data.SubmitImageModel;

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
                SubmitImageData data = response.body().convertDataObj(SubmitImageData.class);
                model.setSuccess(true);
                model.setSubmitImageData(data);
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