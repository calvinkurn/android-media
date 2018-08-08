package com.tokopedia.session.activation.data.mapper;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.activation.data.ChangeEmailModel;
import com.tokopedia.session.activation.data.pojo.ChangeEmailData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailMapper implements Func1<Response<TkpdResponse>, ChangeEmailModel> {

    @Override
    public ChangeEmailModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ChangeEmailModel mappingResponse(Response<TkpdResponse> response) {
        ChangeEmailModel model = new ChangeEmailModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ChangeEmailData data = response.body().convertDataObj(ChangeEmailData.class);
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
