package com.tokopedia.otp.tokocashotp.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.otp.tokocashotp.domain.pojo.requestotp.RequestOtpTokoCashPojo;
import com.tokopedia.otp.tokocashotp.domain.pojo.verifyotp.UserDetail;
import com.tokopedia.otp.tokocashotp.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpTokoCashMapper implements Func1<Response<TkpdDigitalResponse>, VerifyOtpTokoCashViewModel> {

    @Inject
    public VerifyOtpTokoCashMapper() {
    }

    @Override
    public VerifyOtpTokoCashViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            VerifyOtpTokoCashPojo pojo = response.body().convertDataObj(VerifyOtpTokoCashPojo.class);
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

    private VerifyOtpTokoCashViewModel convertToDomain(VerifyOtpTokoCashPojo pojo) {
        return new VerifyOtpTokoCashViewModel(
                pojo.getKey(),
                pojo.isVerified(),
                convertToDomainAccountList(pojo.getUserDetails())
        );
    }

    private ArrayList<AccountTokocash> convertToDomainAccountList(List<UserDetail> userDetails) {
        ArrayList<AccountTokocash> list = new ArrayList<>();
        for (UserDetail pojo : userDetails) {
            list.add(new AccountTokocash(
                    pojo.getTkpdUserId(),
                    pojo.getName(),
                    pojo.getEmail(),
                    pojo.getImage()
            ));
        }
        return list;
    }
}
