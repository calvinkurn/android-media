package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateResoWithoutAttachmentResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ShopResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ResolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ShopDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoWithoutAttachmentMapper
        implements Func1<Response<ResolutionResponse<CreateResoWithoutAttachmentResponse>>, CreateResoWithoutAttachmentDomain> {

    @Inject
    public CreateResoWithoutAttachmentMapper() {
    }

    @Override
    public CreateResoWithoutAttachmentDomain call(Response<ResolutionResponse<CreateResoWithoutAttachmentResponse>> response) {
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

    private ResolutionDomain mappingResolutionDomain(
            com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ResolutionResponse response) {
        return new ResolutionDomain(response.getId());
    }

    private ShopDomain mappingShopDomain(ShopResponse response) {
        return new ShopDomain(response.getName());
    }
}
