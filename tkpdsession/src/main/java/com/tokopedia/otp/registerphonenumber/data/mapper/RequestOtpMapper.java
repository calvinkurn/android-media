package com.tokopedia.otp.registerphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.otp.registerphonenumber.data.pojo.requestotp.RequestOtpResponse;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.session.R;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 5/3/18.
 */

public class RequestOtpMapper implements Func1<Response<TkpdDigitalResponse>, RequestOtpViewModel> {

    private int REQUEST_OTP_LIMIT_REACHED = 412;

    @Inject
    public RequestOtpMapper() {
    }

    @Override
    public RequestOtpViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            RequestOtpResponse pojo = response.body().convertDataObj(RequestOtpResponse.class);
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

    private RequestOtpViewModel convertToDomain(RequestOtpResponse pojo) {
        return new RequestOtpViewModel(
                pojo.getOtpAttemptLeft(),
                pojo.isSent());
    }
}
