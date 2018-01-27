package com.tokopedia.tkpd.tkpdreputation.review.product.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductGetStarCountCloud {

    public static final String PRODUCT_ID = "product_id";
    private ReviewProductApi reputationReviewApi;

    public ReviewProductGetStarCountCloud(ReviewProductApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewStarCount> getReviewStarCount(String productId) {
        return reputationReviewApi.getReviewStarCount(generateParams(productId))
                .map(new GetData<DataResponse<DataResponseReviewStarCount>>())
                .map(new Func1<DataResponse<DataResponseReviewStarCount>, DataResponseReviewStarCount>() {
                    @Override
                    public DataResponseReviewStarCount call(DataResponse<DataResponseReviewStarCount> dataResponseReviewStarCountDataResponse) {
                        return dataResponseReviewStarCountDataResponse.getData();
                    }
                });
    }

    private Map<String,String> generateParams(String productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_ID, productId);
        return params;
    }
}
