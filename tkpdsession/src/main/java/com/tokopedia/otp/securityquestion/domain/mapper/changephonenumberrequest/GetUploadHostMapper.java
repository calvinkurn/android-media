package com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest;

import com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest.UploadHostData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.UploadHostModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/9/17.
 */

public class GetUploadHostMapper implements Func1<Response<TkpdResponse>, UploadHostModel> {

    @Override
    public UploadHostModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadHostModel mappingResponse(Response<TkpdResponse> response) {
        UploadHostModel model = new UploadHostModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadHostData data = response.body().convertDataObj(UploadHostData.class);
                model.setSuccess(true);
                model.setUploadHostData(data);
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
