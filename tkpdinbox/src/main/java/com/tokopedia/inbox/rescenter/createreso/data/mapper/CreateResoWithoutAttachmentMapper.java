package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateResoWithoutAttachmentResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ResolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ShopResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ResolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ShopDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoWithoutAttachmentMapper
        implements Func1<Response<DataResponse<CreateResoWithoutAttachmentResponse>>, CreateResoWithoutAttachmentDomain> {

    @Inject
    public CreateResoWithoutAttachmentMapper() {
    }

    @Override
    public CreateResoWithoutAttachmentDomain call(Response<DataResponse<CreateResoWithoutAttachmentResponse>> response) {
        CreateResoWithoutAttachmentResponse createResoWithoutAttachmentResponse =
                response.body().getData();
        return new CreateResoWithoutAttachmentDomain(
                createResoWithoutAttachmentResponse.getResolution() != null ?
                        mappingResolutionDomain(createResoWithoutAttachmentResponse.getResolution()) :
                        null,
                createResoWithoutAttachmentResponse.getCacheKey(),
                createResoWithoutAttachmentResponse.getShop() != null ?
                        mappingShopDomain(createResoWithoutAttachmentResponse.getShop()) :
                        null,
                createResoWithoutAttachmentResponse.getSuccessMessage());
    }

    private ResolutionDomain mappingResolutionDomain(ResolutionResponse response) {
        return new ResolutionDomain(response.getId());
    }

    private ShopDomain mappingShopDomain(ShopResponse response) {
        return new ShopDomain(response.getName());
    }
}
