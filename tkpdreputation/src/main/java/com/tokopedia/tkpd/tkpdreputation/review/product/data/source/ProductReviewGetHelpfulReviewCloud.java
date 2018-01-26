package com.tokopedia.tkpd.tkpdreputation.review.product.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetHelpfulReviewCloud {
    public static final String PRODUCT_ID = "product_id";
    public static final String SHOP_ID = "shop_id";
    private ReputationReviewApi reputationReviewApi;

    public ProductReviewGetHelpfulReviewCloud(ReputationReviewApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewHelpful> getReviewHelpfulList(String shopId, String productId) {
        return reputationReviewApi.getReviewHelpfulList(generateParams(shopId, productId))
                .map(new GetData<DataResponse<DataResponseReviewHelpful>>())
                .map(new Func1<DataResponse<DataResponseReviewHelpful>, DataResponseReviewHelpful>() {
                    @Override
                    public DataResponseReviewHelpful call(DataResponse<DataResponseReviewHelpful> dataResponseReviewHelpfulDataResponse) {
                        return dataResponseReviewHelpfulDataResponse.getData();
                    }
                });
    }

    private Map<String, String> generateParams(String shopId, String productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_ID, productId);
        params.put(SHOP_ID, shopId);
        return params;
    }
}
