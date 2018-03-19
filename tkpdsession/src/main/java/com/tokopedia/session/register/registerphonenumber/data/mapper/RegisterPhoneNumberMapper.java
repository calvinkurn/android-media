package com.tokopedia.session.register.registerphonenumber.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;
import com.tokopedia.session.register.registerphonenumber.data.pojo.RegisterPhoneNumberData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 28/02/18.
 */

public class RegisterPhoneNumberMapper implements Func1<Response<TkpdResponse>, RegisterPhoneNumberModel> {

    @Inject
    public RegisterPhoneNumberMapper() {
    }

    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";

    @Override
    public RegisterPhoneNumberModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private RegisterPhoneNumberModel mappingResponse(Response<TkpdResponse> response) {
        RegisterPhoneNumberModel model = new RegisterPhoneNumberModel();

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                RegisterPhoneNumberData data = response.body().convertDataObj(RegisterPhoneNumberData.class);
                if (data.getIsSuccess() == 1 || data.getAction() != 0) {
                    model.setSuccess(true);
                    model.setRegisterPhoneNumberData(data);
                } else if (response.body().getErrorMessages().size() > 0)
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());

            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(DEFAULT_ERROR);
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