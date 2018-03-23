package com.tokopedia.session.addchangepassword.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.addchangepassword.data.pojo.AddPasswordResponse;
import com.tokopedia.session.addchangepassword.view.viewmodel.AddPasswordViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordMapper implements Func1<Response<TkpdResponse>, AddPasswordViewModel> {

    @Inject
    public AddPasswordMapper() {
    }

    @Override
    public AddPasswordViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                AddPasswordResponse pojo = response.body().convertDataObj(AddPasswordResponse
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

    private AddPasswordViewModel mappingToViewModel(AddPasswordResponse response) {
        return new AddPasswordViewModel(response.getIsSuccess() == 1);
    }
}
