package com.tokopedia.session.register.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.register.data.pojo.RegisterValidationPojo;
import com.tokopedia.session.register.view.viewmodel.RegisterValidationViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alvinatin on 20/06/18.
 */

public class RegisterValidationMapper implements Func1<Response<TkpdResponse>,
        RegisterValidationViewModel> {

    @Inject
    public RegisterValidationMapper() {
    }

    @Override
    public RegisterValidationViewModel call(Response<TkpdResponse>
                                                        registerValidationPojoResponse) {
        return checkIfError(registerValidationPojoResponse);
    }

    private RegisterValidationViewModel checkIfError(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                throw new ErrorMessageException("");
            }
            else if (TextUtils.isEmpty(response.body().getErrorMessageJoined())){
                RegisterValidationPojo pojo = response.body().
                        convertDataObj(RegisterValidationPojo.class);
                return mapToViewModel(pojo);
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if(!TextUtils.isEmpty(messageError)){
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private RegisterValidationViewModel mapToViewModel(RegisterValidationPojo data) {
        return new RegisterValidationViewModel(data.getExist(), data.getType(), data.getView());
    }
}
