package com.tokopedia.session.login.loginphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.session.login.loginphonenumber.domain.pojo.checkmsisdn.CheckMsisdnTokoCashPojo;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.CheckMsisdnTokoCashViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashMapper implements Func1<Response<TkpdDigitalResponse>, CheckMsisdnTokoCashViewModel> {

    @Inject
    public CheckMsisdnTokoCashMapper() {
    }

    @Override
    public CheckMsisdnTokoCashViewModel call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            CheckMsisdnTokoCashPojo pojo = response.body().convertDataObj(CheckMsisdnTokoCashPojo.class);
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

    private CheckMsisdnTokoCashViewModel convertToDomain(CheckMsisdnTokoCashPojo pojo) {
        return new CheckMsisdnTokoCashViewModel(pojo.isTokocashAccountExist(), pojo.isTokopediaAccountExist());
    }
}
