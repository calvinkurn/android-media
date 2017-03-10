package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.core.network.entity.changephonenumberrequest.CheckStatusData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.changephonenumber.data.CheckStatusModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/10/17.
 */

public class CheckStatusMapper implements Func1<Response<TkpdResponse>, CheckStatusModel> {

    @Override
    public CheckStatusModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private CheckStatusModel mappingResponse(Response<TkpdResponse> response) {
        CheckStatusModel model = new CheckStatusModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                CheckStatusData data = response.body().convertDataObj(CheckStatusData.class);
                model.setSuccess(true);
                model.setCheckStatusData(data);
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
