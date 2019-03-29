package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateValidateResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateValidateMapper implements Func1<Response<ResolutionResponse<CreateValidateResponse>>, CreateValidateDomain> {

    @Inject
    public CreateValidateMapper() {
    }

    @Override
    public CreateValidateDomain call(Response<ResolutionResponse<CreateValidateResponse>> response) {
        return mappingResponse(response);
    }

    private CreateValidateDomain mappingResponse(Response<ResolutionResponse<CreateValidateResponse>> response) {
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        CreateValidateResponse createValidateResponse = response.body().getData();
        return  new CreateValidateDomain(createValidateResponse.getCacheKey());
    }
}
