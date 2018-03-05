package com.tokopedia.otp.registerphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.otp.registerphonenumber.data.pojo.requestotp.RequestOtpResponse;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.RequestOtpViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 5/3/18.
 */

public class RequestOtpMapper implements Func1<Response<TkpdResponse>, RequestOtpViewModel> {

    private int REQUEST_OTP_LIMIT_REACHED = 412;

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
                RequestOtpResponse pojo = response.body().convertDataObj(RequestOtpResponse.class);
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

    private RequestOtpViewModel convertToDomain(RequestOtpResponse pojo, Response<TkpdResponse> response) {
        return new RequestOtpViewModel(pojo.isSuccess(), response.body().getStatusMessageJoined());
    }
}
