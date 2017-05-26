package com.tokopedia.session.login.domain.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.otp.RequestOtpData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.login.domain.model.MakeLoginDomainData;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;
import com.tokopedia.session.login.domain.pojo.MakeLoginData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginMapper implements Func1<Response<TkpdResponse>, MakeLoginDomainModel> {

    @Override
    public MakeLoginDomainModel call(Response<TkpdResponse> response) {
        return convertToDomainModel(response);
    }

    private MakeLoginDomainModel convertToDomainModel(Response<TkpdResponse> response) {
        MakeLoginDomainModel model = new MakeLoginDomainModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                MakeLoginData data = response.body().convertDataObj(MakeLoginData.class);
                model.setSuccess(true);
                model.setMakeLoginDomainData(convertToDomainData(data));
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

    private MakeLoginDomainData convertToDomainData(MakeLoginData data) {
        return new MakeLoginDomainData();
    }
}
