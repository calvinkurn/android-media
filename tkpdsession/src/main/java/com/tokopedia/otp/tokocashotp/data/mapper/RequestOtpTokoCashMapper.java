package com.tokopedia.otp.tokocashotp.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.otp.tokocashotp.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.otp.tokocashotp.view.viewmodel.RequestOtpTokoCashViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashMapper implements Func1<Response<TkpdDigitalResponse>, RequestOtpTokoCashViewModel> {

    @Inject
    public RequestOtpTokoCashMapper() {
    }

    @Override
    public RequestOtpTokoCashViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            RequestOtpTokoCashPojo pojo = response.body().convertDataObj(RequestOtpTokoCashPojo.class);
            return convertToDomain(pojo);
        } else {
            String messageError = ErrorHandler.getErrorMessageTokoCash(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private RequestOtpTokoCashViewModel convertToDomain(RequestOtpTokoCashPojo pojo) {
        return new RequestOtpTokoCashViewModel(
                pojo.getOtpAttemptLeft(),
                pojo.isSent());
    }
}
