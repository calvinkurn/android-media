package com.tokopedia.session.domain.mapper;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.domain.pojo.token.ErrorModel;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/11/17.
 */

public class TokenMapper implements Func1<Response<String>, TokenViewModel> {

    @Inject
    public TokenMapper() {
    }

    @Override
    public TokenViewModel call(Response<String> response) {
        if (response.isSuccessful()) {
            String stringResponse = String.valueOf(response.body());
            ErrorModel errorModel = new GsonBuilder().create().fromJson(stringResponse, ErrorModel.class);
            if (errorModel != null
                    && errorModel.getErrorDescription() != null) {
                throw new ErrorMessageException(errorModel.getErrorDescription());
            } else {
                return new GsonBuilder().create().fromJson(stringResponse, TokenViewModel
                        .class);
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
}
