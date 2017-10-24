package com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest.CheckStatusData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.CheckStatusModel;

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
