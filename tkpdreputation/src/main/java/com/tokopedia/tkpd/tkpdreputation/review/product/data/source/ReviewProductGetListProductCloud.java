package com.tokopedia.tkpd.tkpdreputation.review.product.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductGetListProductCloud {

    public static final String PRODUCT_ID = "product_id";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String RATING = "rating";
    private ReviewProductApi reputationReviewApi;

    public ReviewProductGetListProductCloud(ReviewProductApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewProduct> getReviewProductList(String productId, String page, String perPage, String rating) {

        return reputationReviewApi.getReviewProductList(generateMapParams(productId,page, perPage, rating))
                .map(new GetData<DataResponse<DataResponseReviewProduct>>())
                .map(new Func1<DataResponse<DataResponseReviewProduct>, DataResponseReviewProduct>() {
                    @Override
                    public DataResponseReviewProduct call(DataResponse<DataResponseReviewProduct> dataResponseReviewProductDataResponse) {
                        return dataResponseReviewProductDataResponse.getData();
                    }
                });
    }

    private Map<String, String> generateMapParams(String productId, String page, String perPage, String rating) {
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_ID, productId);
        params.put(PAGE, page);
        params.put(PER_PAGE, perPage);
        params.put(RATING, rating);
        return params;
    }
}
