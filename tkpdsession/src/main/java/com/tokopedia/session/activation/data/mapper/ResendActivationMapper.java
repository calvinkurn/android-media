package com.tokopedia.session.activation.data.mapper;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.activation.data.ResendActivationModel;
import com.tokopedia.session.activation.data.pojo.ResendActivationData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationMapper implements Func1<Response<TkpdResponse>, ResendActivationModel> {

    @Override
    public ResendActivationModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ResendActivationModel mappingResponse(Response<TkpdResponse> response) {
        ResendActivationModel model = new ResendActivationModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ResendActivationData data = response.body().convertDataObj(ResendActivationData.class);
                model.setSuccess(data.getIsSuccess() == 1);
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
