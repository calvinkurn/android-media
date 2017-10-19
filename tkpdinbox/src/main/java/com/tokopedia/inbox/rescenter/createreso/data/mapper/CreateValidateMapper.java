package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import android.content.Context;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateValidateResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateValidateMapper implements Func1<Response<TkpdResponse>, CreateValidateDomain> {

    private Context context;

    public CreateValidateMapper(Context context) {
        this.context = context;
    }

    @Override
    public CreateValidateDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private CreateValidateDomain mappingResponse(Response<TkpdResponse> response) {
        CreateValidateResponse createValidateResponse = response.body().convertDataObj(
                CreateValidateResponse.class);
        CreateValidateDomain model = new CreateValidateDomain(createValidateResponse.getCacheKey());
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(context.getResources().getString(R.string.string_general_error));
                }
            } else {
                model.setSuccess(true);
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
