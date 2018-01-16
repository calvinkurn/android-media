package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetListProductCloud {

    private ReputationReviewApi reputationReviewApi;

    public ProductReviewGetListProductCloud(ReputationReviewApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewProduct> getReviewProductList(RequestParams requestParams) {
        return reputationReviewApi.getReviewProductList(requestParams.getParameters())
                .map(new Func1<Response<DataResponseReviewProduct>, DataResponseReviewProduct>() {
                    @Override
                    public DataResponseReviewProduct call(Response<DataResponseReviewProduct> dataResponseReviewProductResponse) {
                        return null;
                    }
                });
    }
}
