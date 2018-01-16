package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.DataResponseReviewCount;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetCountCloud {

    private ReputationReviewApi reputationReviewApi;

    public ProductReviewGetCountCloud(ReputationReviewApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewCount> getReviewCount(RequestParams requestParams) {
        return reputationReviewApi.getReviewCount("",requestParams.getParameters())
                .map(new Func1<Response<DataResponseReviewCount>, DataResponseReviewCount>() {
                    @Override
                    public DataResponseReviewCount call(Response<DataResponseReviewCount> dataResponseReviewCountResponse) {
                        return null;
                    }
                });
    }
}
