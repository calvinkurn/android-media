package com.tokopedia.tkpd.tkpdreputation.productreview.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewGetHelpfulReviewCloud {
    private ReputationReviewApi reputationReviewApi;

    public ProductReviewGetHelpfulReviewCloud(ReputationReviewApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }

    public Observable<DataResponseReviewHelpful> getReviewHelpfulList(RequestParams requestParams) {
        return reputationReviewApi.getReviewHelpfulList(requestParams.getParameters())
                .map(new Func1<Response<DataResponseReviewHelpful>, DataResponseReviewHelpful>() {
                    @Override
                    public DataResponseReviewHelpful call(Response<DataResponseReviewHelpful> dataResponseReviewHelpfulResponse) {
                        return null;
                    }
                });
    }
}
