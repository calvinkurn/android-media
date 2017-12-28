package com.tokopedia.otp.cotp.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.otp.cotp.domain.pojo.requestotp.RequestOtpPojo;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.session.R;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpMapper implements Func1<Response<TkpdDigitalResponse>, RequestOtpViewModel> {

    private int REQUEST_OTP_LIMIT_REACHED = 412;

    @Inject
    public RequestOtpMapper() {
    }

    @Override
    public RequestOtpViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            RequestOtpPojo pojo = response.body().convertDataObj(RequestOtpPojo.class);
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

    private RequestOtpViewModel convertToDomain(RequestOtpPojo pojo) {
        return new RequestOtpViewModel(
                pojo.getOtpAttemptLeft(),
                pojo.isSent());
    }
}
