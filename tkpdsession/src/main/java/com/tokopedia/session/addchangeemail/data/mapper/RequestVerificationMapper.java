package com.tokopedia.session.addchangeemail.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.addchangeemail.data.pojo.RequestVerificationResponse;
import com.tokopedia.session.addchangeemail.view.viewmodel.RequestVerificationViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 09/03/18.
 */

public class RequestVerificationMapper implements Func1<Response<TkpdResponse>, RequestVerificationViewModel> {

    @Inject
    public RequestVerificationMapper() {
    }

    @Override
    public RequestVerificationViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                RequestVerificationResponse pojo = response.body().convertDataObj(RequestVerificationResponse
                        .class);
                return mappingToViewModel(pojo);
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

    private RequestVerificationViewModel mappingToViewModel(RequestVerificationResponse response) {
        return new RequestVerificationViewModel(response.getIsSuccess() == 1);
    }
}
