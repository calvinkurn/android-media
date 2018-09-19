package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import retrofit2.Response;
import rx.Observable;

public class ProductWishlistUrlUseCase extends UseCase<Response<String>> {

    public static final String PRODUCT_WISHLIST_URL = "product_wishlist_url";
    private final TopAdsService topAdsService;
    private final Context context;

    public ProductWishlistUrlUseCase(TopAdsService topAdsService, Context context) {
        this.topAdsService = topAdsService;
        this.context = context;
    }

    @Override
    public Observable<Response<String>> createObservable(RequestParams requestParams) {
        return topAdsService.productWishlistUrl(requestParams.getString(PRODUCT_WISHLIST_URL, ""));
    }
}
