package com.tokopedia.session.login.loginphonenumber.data.mapper;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.domain.pojo.token.ErrorModel;
import com.tokopedia.session.login.loginphonenumber.domain.model.AccessTokenTokoCashDomain;
import com.tokopedia.session.login.loginphonenumber.domain.pojo.GetAccessTokenTokoCashPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/6/17.
 */

public class WalletTokenMapper implements Func1<Response<String>, AccessTokenTokoCashDomain>{

        @Inject
        public WalletTokenMapper() {
        }

        @Override
        public AccessTokenTokoCashDomain call(Response<String> response) {
            if (response.isSuccessful()) {
                String stringResponse = String.valueOf(response.body());
                ErrorModel errorModel = new GsonBuilder().create().fromJson(stringResponse, ErrorModel.class);
                if (errorModel != null
                        && errorModel.getErrorDescription() != null) {
                    throw new ErrorMessageException(errorModel.getErrorDescription());
                } else {
                    return convertToDomain(new GsonBuilder().create().fromJson(stringResponse, GetAccessTokenTokoCashPojo
                            .class));
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

        private AccessTokenTokoCashDomain convertToDomain(GetAccessTokenTokoCashPojo getAccessTokenTokoCashPojo) {
            return new AccessTokenTokoCashDomain(getAccessTokenTokoCashPojo.getAccessToken(),
                    getAccessTokenTokoCashPojo.getExpiresIn(),
                    getAccessTokenTokoCashPojo.getScope(),
                    getAccessTokenTokoCashPojo.getTokenType());
        }
}

