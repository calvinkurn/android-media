package com.tokopedia.tkpdpdp.domain;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.product.ReputationReviewService;
import com.tokopedia.core.network.apiservices.product.apis.ReputationReviewApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.model.productdetail.mosthelpful.MostHelpfulReviewResponse;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class GetMostHelpfulReviewUseCase extends UseCase<List<Review>>{

    public static final String PRODUCT_ID_PARAM = "productId";
    public static final String SHOP_ID = "shopId";
    private final ReputationReviewService service;

    public GetMostHelpfulReviewUseCase() {
        service = new ReputationReviewService();
    }

    @Override
    public Observable<List<Review>> createObservable(RequestParams requestParams) {
        String productId = requestParams.getString(PRODUCT_ID_PARAM, "");
        String shopId = requestParams.getString(SHOP_ID, "");

        return service.getApi().getMostHelpfulReview(
                AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(),
                        getMostHelpfulParam(productId, shopId))
        ).map(new Func1<Response<MostHelpfulReviewResponse>, List<Review>>() {
            @Override
            public List<Review> call(Response<MostHelpfulReviewResponse> mostHelpfulReviewResponseResponse) {
                return mostHelpfulReviewResponseResponse.body().getData().getReviews();
            }
        });
    }

    private TKPDMapParam getMostHelpfulParam(String productId, String shopId) {
        com.tokopedia.core.base.domain.RequestParams params = com.tokopedia.core.base.domain.RequestParams.create();
        params.putString(ReputationReviewApi.ID, productId);
        params.putString(ReputationReviewApi.SHOP_ID, shopId);
        params.putString(ReputationReviewApi.PER_PAGE, String.valueOf(1));
        params.putString(ReputationReviewApi.PARAM_SOURCE, ReputationReviewApi.VALUE_SNEAK_PEAK);
        return params.getParameters();
    }
}
