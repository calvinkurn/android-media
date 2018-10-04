package com.tokopedia.tkpdpdp.domain;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.network.entity.affiliateProductData.AffiliateProductDataResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import retrofit2.Response;
import rx.Observable;

public class GetAffiliateProductDataUseCase extends UseCase<Response<AffiliateProductDataResponse>> {
    public static final String UI_PARAM = "uiParam";
    public static final String USER_ID_PARAM = "userIdParam";
    public static final String SHOP_ID_PARAM = "shopIdParam";
    public static final String PRODUCT_ID_PARAM = "productIdParam";

    public static final String PARAMS_REQUEST_UI = "1";
    public static final String PARAMS_NOT_REQUEST_UI = "0";

    public static final String PARAMS_GUEST_USER_ID = "0";
    public static final String PARAMS_DEFAULT_SHOP_ID = "1";
    public static final String PARAMS_DEFAULT_PRODUCT_ID = "1";

    private final TopAdsService service;

    public GetAffiliateProductDataUseCase(TopAdsService topAdsService) {
        this.service = topAdsService;
    }

    @Override
    public Observable<Response<AffiliateProductDataResponse>> createObservable(RequestParams requestParams) {
        String uiParam = requestParams.getString(UI_PARAM, PARAMS_REQUEST_UI);
        String userIdParam = requestParams.getString(USER_ID_PARAM, PARAMS_GUEST_USER_ID);
        String shopIdParam = requestParams.getString(SHOP_ID_PARAM, PARAMS_DEFAULT_SHOP_ID);
        String productIdParam = requestParams.getString(PRODUCT_ID_PARAM, PARAMS_DEFAULT_PRODUCT_ID);

        return service.getPdpAffiliateData(
                uiParam,
                userIdParam,
                shopIdParam,
                productIdParam
        );
    }
}
