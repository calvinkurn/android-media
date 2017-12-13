package com.tokopedia.otp.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.domain.pojo.RequestOtpPojo;
import com.tokopedia.session.R;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class RequestOtpMapper implements Func1<Response<TkpdResponse>, RequestOtpViewModel> {

    @Inject
    public RequestOtpMapper() {
    }

    @Override
    public RequestOtpViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                RequestOtpPojo pojo = response.body().convertDataObj(RequestOtpPojo.class);
                return convertToDomain(pojo, response);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private RequestOtpViewModel convertToDomain(RequestOtpPojo pojo, Response<TkpdResponse> response) {
        return new RequestOtpViewModel(pojo.isSuccess(), response.body().getStatusMessageJoined());
    }
}
