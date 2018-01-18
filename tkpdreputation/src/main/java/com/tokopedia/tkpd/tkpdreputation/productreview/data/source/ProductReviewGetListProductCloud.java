package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.AuthUtil;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetListProductCloud {

    public static final String PRODUCT_ID = "product_id";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String RATING = "rating";
    private ReputationReviewApi reputationReviewApi;

    public ProductReviewGetListProductCloud(ReputationReviewApi reputationReviewApi) {
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
