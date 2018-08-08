package com.tokopedia.session.register.data.mapper;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.register.data.model.RegisterEmailModel;
import com.tokopedia.session.register.data.pojo.RegisterEmailData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailMapper implements Func1<Response<TkpdResponse>, RegisterEmailModel> {

    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";

    @Override
    public RegisterEmailModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private RegisterEmailModel mappingResponse(Response<TkpdResponse> response) {
        RegisterEmailModel model = new RegisterEmailModel();

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                RegisterEmailData data = response.body().convertDataObj(RegisterEmailData.class);
                if (data.getIsSuccess() == 1 || data.getAction() != 0) {
                    model.setSuccess(true);
                    model.setRegisterEmailData(data);
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
