package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateSubmitResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ResolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ShopResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ResolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ShopDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateSubmitMapper implements Func1<Response<TkpdResponse>, CreateSubmitDomain> {

    @Override
    public CreateSubmitDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private CreateSubmitDomain mappingResponse(Response<TkpdResponse> response) {
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
        CreateSubmitResponse createSubmitResponse = response.body()
                .convertDataObj(CreateSubmitResponse.class);
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
