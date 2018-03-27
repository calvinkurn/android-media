package com.tokopedia.session.changename.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.changename.data.pojo.ChangeNameResponse;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameMapper implements Func1<Response<TkpdResponse>, ChangeNameViewModel> {

    @Inject
    public ChangeNameMapper() {
    }

    @Override
    public ChangeNameViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().getErrorMessages() != null
                    && !response.body().getErrorMessages().isEmpty()) {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            } else if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                ChangeNameResponse pojo = response.body().convertDataObj(ChangeNameResponse
                        .class);
                return mappingToViewModel(pojo);
            } else {
                throw new ErrorMessageException("");
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

    private ChangeNameViewModel mappingToViewModel(ChangeNameResponse response) {
        return new ChangeNameViewModel(response.getIsSuccess() == 1);
    }
}
