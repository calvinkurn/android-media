package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.core.network.entity.changephonenumberrequest.GeneratePostKeyData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.changephonenumber.data.GeneratePostKeyModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/9/17.
 */

public class GeneratePostKeyMapper implements Func1<Response<TkpdResponse>, GeneratePostKeyModel> {


    @Override
    public GeneratePostKeyModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private GeneratePostKeyModel mappingResponse(Response<TkpdResponse> response) {
        GeneratePostKeyModel model = new GeneratePostKeyModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                GeneratePostKeyData data = response.body().convertDataObj(GeneratePostKeyData.class);
                model.setSuccess(true);
                model.setGeneratePostKeyData(data);
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
