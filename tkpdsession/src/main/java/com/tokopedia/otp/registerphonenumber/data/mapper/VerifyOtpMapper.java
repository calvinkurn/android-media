package com.tokopedia.otp.registerphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.otp.registerphonenumber.data.pojo.verifyotp.UserDetailResponse;
import com.tokopedia.otp.registerphonenumber.data.pojo.verifyotp.VerifyOtpResponse;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.VerifyOtpViewModel;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 5/3/17.
 */

public class VerifyOtpMapper implements Func1<Response<TkpdDigitalResponse>, VerifyOtpViewModel> {

    @Inject
    public VerifyOtpMapper() {
    }

    @Override
    public VerifyOtpViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            VerifyOtpResponse pojo = response.body().convertDataObj(VerifyOtpResponse.class);
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

    private VerifyOtpViewModel convertToDomain(VerifyOtpResponse pojo) {
        return new VerifyOtpViewModel(
                pojo.getKey(),
                pojo.isVerified(),
                convertToDomainAccountList(pojo.getUserDetailResponses())
        );
    }

    private ArrayList<AccountTokocash> convertToDomainAccountList(List<UserDetailResponse> userDetails) {
        ArrayList<AccountTokocash> list = new ArrayList<>();
        for (UserDetailResponse pojo : userDetails) {
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
