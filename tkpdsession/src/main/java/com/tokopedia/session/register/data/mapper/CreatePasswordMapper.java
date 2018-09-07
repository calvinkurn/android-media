package com.tokopedia.session.register.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.R;
import com.tokopedia.session.register.data.pojo.CreatePasswordPojo;
import com.tokopedia.session.register.domain.model.CreatePasswordDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordMapper implements Func1<Response<TkpdResponse>, CreatePasswordDomain> {

    @Inject
    public CreatePasswordMapper() {
    }

    @Override
    public CreatePasswordDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                CreatePasswordPojo pojo = response.body().convertDataObj(CreatePasswordPojo.class);
                return mappingToViewModel(pojo);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
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

    private CreatePasswordDomain mappingToViewModel(CreatePasswordPojo pojo) {
        return new CreatePasswordDomain(pojo.getIsSuccess() == 1);
    }
}
