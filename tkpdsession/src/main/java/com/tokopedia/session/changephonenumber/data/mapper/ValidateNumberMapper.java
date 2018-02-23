package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.changephonenumber.data.model.ValidateNumberData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by milhamj on 03/01/18.
 */

public class ValidateNumberMapper implements Func1<Response<TkpdResponse>, Boolean> {
    @Inject
    public ValidateNumberMapper() {
    }

    @Override
    public Boolean call(Response<TkpdResponse> tkpdResponseResponse) {
        Boolean model;
        if (tkpdResponseResponse.isSuccessful()) {
            if (!tkpdResponseResponse.body().isError() &&
                    (tkpdResponseResponse.body().getErrorMessageJoined().isEmpty() ||
                            tkpdResponseResponse.body().getErrorMessages() == null)
                    ) {
                ValidateNumberData data = tkpdResponseResponse.body().convertDataObj(
                        ValidateNumberData.class);
                model = (data.getIsSuccess() == 1);
            } else {
                if (tkpdResponseResponse.body().getErrorMessages() != null &&
                        !tkpdResponseResponse.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(
                            tkpdResponseResponse.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(tkpdResponseResponse.code()));
        }

        return model;
    }
}
