package com.tokopedia.tkpdpdp.domain;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.entity.wishlistCount.WishlistCountResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import retrofit2.Response;
import rx.Observable;

public class GetWishlistCountUseCase extends UseCase<Response<WishlistCountResponse>>{

    public static final String PRODUCT_ID_PARAM = "productId";
    private final MojitoApi service;

    public GetWishlistCountUseCase(MojitoApi service) {
        this.service = service;
    }

    @Override
    public Observable<Response<WishlistCountResponse>> createObservable(RequestParams requestParams) {
        return service.getWishlistCount(requestParams.getString(PRODUCT_ID_PARAM, "0"));
    }
}
