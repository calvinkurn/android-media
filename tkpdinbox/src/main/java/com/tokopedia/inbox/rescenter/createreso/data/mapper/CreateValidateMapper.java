package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateValidateResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.network.ErrorMessageException.DEFAULT_ERROR;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateValidateMapper implements Func1<Response<TkpdResponse>, CreateValidateDomain> {

    @Override
    public CreateValidateDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private CreateValidateDomain mappingResponse(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        CreateValidateResponse createValidateResponse = response.body().convertDataObj(
                CreateValidateResponse.class);
        return  new CreateValidateDomain(createValidateResponse.getCacheKey());
    }
}
