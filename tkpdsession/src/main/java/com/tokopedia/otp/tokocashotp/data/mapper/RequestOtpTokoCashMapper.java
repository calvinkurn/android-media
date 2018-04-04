package com.tokopedia.otp.tokocashotp.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.otp.tokocashotp.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.otp.tokocashotp.view.viewmodel.RequestOtpTokoCashViewModel;
import com.tokopedia.session.R;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashMapper implements Func1<Response<TkpdDigitalResponse>, RequestOtpTokoCashViewModel> {

    private int REQUEST_OTP_LIMIT_REACHED = 412;

    @Inject
    public RequestOtpTokoCashMapper() {
    }

    @Override
    public RequestOtpTokoCashViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            RequestOtpTokoCashPojo pojo = response.body().convertDataObj(RequestOtpTokoCashPojo.class);
            return convertToDomain(pojo);
        } else if (response.code() == REQUEST_OTP_LIMIT_REACHED) {
            throw new ErrorMessageException(MainApplication.getAppContext().getString(R.string
                    .limit_otp_reached));
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
