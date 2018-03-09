package com.tokopedia.session.addchangeemail.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.addchangeemail.data.pojo.CheckEmailResponse;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailMapper implements Func1<Response<TkpdResponse>, CheckEmailViewModel> {

    @Inject
    public CheckEmailMapper() {
    }

    @Override
    public CheckEmailViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                CheckEmailResponse pojo = response.body().convertDataObj(CheckEmailResponse
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

    private CheckEmailViewModel mappingToViewModel(CheckEmailResponse response) {
        return new CheckEmailViewModel(response.getIsSuccess() == 1);
    }
}
