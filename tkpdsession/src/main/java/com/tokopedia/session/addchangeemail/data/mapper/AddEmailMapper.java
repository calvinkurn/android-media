package com.tokopedia.session.addchangeemail.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.addchangeemail.data.pojo.AddEmailResponse;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailMapper implements Func1<Response<TkpdResponse>, AddEmailViewModel> {

    @Inject
    public AddEmailMapper() {
    }

    @Override
    public AddEmailViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                throw new ErrorMessageException("");
            }
            else if (TextUtils.isEmpty(response.body().getErrorMessageJoined())) {
                AddEmailResponse pojo = response.body().convertDataObj(AddEmailResponse
                        .class);
                return mappingToViewModel(pojo);
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            throw new ErrorMessageException(messageError);
        }
    }

    private AddEmailViewModel mappingToViewModel(AddEmailResponse response) {
        return new AddEmailViewModel(response.getIsSuccess() == 1);
    }
}
