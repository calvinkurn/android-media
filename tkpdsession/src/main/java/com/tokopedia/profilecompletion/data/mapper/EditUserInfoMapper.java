package com.tokopedia.profilecompletion.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.profilecompletion.data.pojo.EditUserInfoData;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 7/3/17.
 */

public class EditUserInfoMapper implements Func1<Response<TkpdResponse>, EditUserInfoDomainModel> {

    @Override
    public EditUserInfoDomainModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private EditUserInfoDomainModel mappingResponse(Response<TkpdResponse> response) {
        EditUserInfoDomainModel model = new EditUserInfoDomainModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                EditUserInfoData data = response.body().convertDataObj(EditUserInfoData.class);
                model.setSuccess(data.getIsSuccess() == 1);
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
