package com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest;

import com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest.UploadImageData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.UploadImageModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/10/17.
 */

public class UploadImageMapper implements Func1<Response<TkpdResponse>, UploadImageModel> {

    @Override
    public UploadImageModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadImageModel mappingResponse(Response<TkpdResponse> response) {
        UploadImageModel model = new UploadImageModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadImageData data = response.body().convertDataObj(UploadImageData.class);
                model.setSuccess(true);
                model.setUploadImageData(data);
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
