package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.changephonenumber.data.model.SendEmailData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by milhamj on 28/12/17.
 */

public class SendEmailMapper implements Func1<Response<TkpdResponse>, Boolean> {
    @Inject
    public SendEmailMapper() {
    }

    @Override
    public Boolean call(Response<TkpdResponse> tkpdResponseResponse) {
        Boolean model;
        if (tkpdResponseResponse.isSuccessful()) {
            if (!tkpdResponseResponse.body().isError() &&
                    (tkpdResponseResponse.body().getErrorMessageJoined().isEmpty() ||
                            tkpdResponseResponse.body().getErrorMessages() == null)
                    ) {
                SendEmailData data = tkpdResponseResponse.body().convertDataObj(
                        SendEmailData.class);
                model = (data.getIsSuccess() == 1);
            } else {
                if (tkpdResponseResponse.body().getErrorMessages() != null &&
                        !tkpdResponseResponse.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(
                            tkpdResponseResponse.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(
                            ErrorMessageException.DEFAULT_ERROR);
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(tkpdResponseResponse.code()));
        }

        return model;
    }
}
