package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateSubmitResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ResolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ShopResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ResolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ShopDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateSubmitMapper implements Func1<Response<DataResponse<CreateSubmitResponse>>, CreateSubmitDomain> {

    @Inject
    public CreateSubmitMapper() {
    }

    @Override
    public CreateSubmitDomain call(Response<DataResponse<CreateSubmitResponse>> dataResponseResponse) {
        return null;
    }

    private CreateSubmitDomain mappingResponse(Response<DataResponse<CreateSubmitResponse>> response) {
        CreateSubmitResponse createSubmitResponse = response.body().getData();
        return new CreateSubmitDomain(
                createSubmitResponse.getResolution() != null ?
                        mappingResolutionDomain(createSubmitResponse.getResolution()) : null,
                createSubmitResponse.getShop() != null ?
                        mappingShopDomain(createSubmitResponse.getShop()) : null,
                createSubmitResponse.getSuccessMessage());
    }

    private ResolutionDomain mappingResolutionDomain(ResolutionResponse response) {
        return new ResolutionDomain(response.getId());
    }

    private ShopDomain mappingShopDomain(ShopResponse response) {
        return new ShopDomain(response.getName());
    }
}
