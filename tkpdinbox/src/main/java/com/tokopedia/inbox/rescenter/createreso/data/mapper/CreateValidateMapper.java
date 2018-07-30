package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateValidateResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateValidateMapper implements Func1<Response<DataResponse<CreateValidateResponse>>, CreateValidateDomain> {

    @Inject
    public CreateValidateMapper() {
    }

    @Override
    public CreateValidateDomain call(Response<DataResponse<CreateValidateResponse>> response) {
        return mappingResponse(response);
    }

    private CreateValidateDomain mappingResponse(Response<DataResponse<CreateValidateResponse>> response) {
        CreateValidateResponse createValidateResponse = response.body().getData();
        return  new CreateValidateDomain(createValidateResponse.getCacheKey());
    }
}
