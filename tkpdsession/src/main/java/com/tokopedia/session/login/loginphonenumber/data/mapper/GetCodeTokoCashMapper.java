package com.tokopedia.session.login.loginphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.session.login.loginphonenumber.domain.model.CodeTokoCashDomain;
import com.tokopedia.session.login.loginphonenumber.domain.pojo.accesstoken.GetCodeTokoCashPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/5/17.
 */

public class GetCodeTokoCashMapper implements Func1<Response<TkpdDigitalResponse>,
        CodeTokoCashDomain> {

    @Inject
    public GetCodeTokoCashMapper() {
    }

    @Override
    public CodeTokoCashDomain call(Response<TkpdDigitalResponse> response) {
        if (response.isSuccessful()) {
            GetCodeTokoCashPojo pojo = response.body().convertDataObj(GetCodeTokoCashPojo.class);
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

    private CodeTokoCashDomain convertToDomain(GetCodeTokoCashPojo pojo) {
        return new CodeTokoCashDomain(pojo.getCode());
    }
}
