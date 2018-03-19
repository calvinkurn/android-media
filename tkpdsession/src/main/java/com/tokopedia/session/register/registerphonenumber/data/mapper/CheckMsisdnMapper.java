package com.tokopedia.session.register.registerphonenumber.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.register.registerphonenumber.data.pojo.CheckMsisdnResponse;
import com.tokopedia.session.register.registerphonenumber.domain.model.CheckMsisdnDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnMapper implements Func1<Response<TkpdResponse>, CheckMsisdnDomain> {

    @Inject
    public CheckMsisdnMapper() {
    }

    @Override
    public CheckMsisdnDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                CheckMsisdnResponse pojo = response.body().convertDataObj(CheckMsisdnResponse
                        .class);
                return mappingToViewModel(pojo, response.body().getStatusMessageJoined());
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
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

    private CheckMsisdnDomain mappingToViewModel(CheckMsisdnResponse pojo,
                                                          String statusMessage) {
        return new CheckMsisdnDomain(pojo.isExist());
    }
}