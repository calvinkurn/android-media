package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetStarCountCloud {

    private ReputationReviewApi reputationReviewApi;

    public ProductReviewGetStarCountCloud(ReputationReviewApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewStarCount> getReviewStarCount(RequestParams requestParams) {
        return reputationReviewApi.getReviewStarCount(requestParams.getParameters())
                .map(new Func1<Response<DataResponseReviewStarCount>, DataResponseReviewStarCount>() {
                    @Override
                    public DataResponseReviewStarCount call(Response<DataResponseReviewStarCount> dataResponseReviewStarCountResponse) {
                        return null;
                    }
                });
    }
}
