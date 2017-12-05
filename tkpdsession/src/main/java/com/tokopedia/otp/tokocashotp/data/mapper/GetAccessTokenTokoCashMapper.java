package com.tokopedia.otp.tokocashotp.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.otp.tokocashotp.domain.pojo.accesstoken.GetAccessTokenTokoCashPojo;
import com.tokopedia.otp.tokocashotp.domain.model.AccessTokenTokoCashDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/5/17.
 */

public class GetAccessTokenTokoCashMapper implements Func1<Response<TkpdDigitalResponse>,
        AccessTokenTokoCashDomain> {

    @Inject
    public GetAccessTokenTokoCashMapper() {
    }

    @Override
    public AccessTokenTokoCashDomain call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            GetAccessTokenTokoCashPojo pojo = response.body().convertDataObj(GetAccessTokenTokoCashPojo.class);
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

    private AccessTokenTokoCashDomain convertToDomain(GetAccessTokenTokoCashPojo pojo) {
        return new AccessTokenTokoCashDomain(pojo.getCode());
    }
}
