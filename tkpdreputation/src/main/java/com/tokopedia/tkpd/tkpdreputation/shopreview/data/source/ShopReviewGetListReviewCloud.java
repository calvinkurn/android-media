package com.tokopedia.tkpd.tkpdreputation.shopreview.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.source.ReputationReviewApi;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ShopReviewGetListReviewCloud {
    private ReputationReviewApi reputationReviewApi;

    public ShopReviewGetListReviewCloud(ReputationReviewApi reputationReviewApi) {
        this.reputationReviewApi = reputationReviewApi;
    }


    public Observable<DataResponseReviewShop> getReviewShopList(RequestParams requestParams) {
        return reputationReviewApi.getReviewShopList(requestParams.getParameters())
                .map(new Func1<Response<DataResponseReviewShop>, DataResponseReviewShop>() {
                    @Override
                    public DataResponseReviewShop call(Response<DataResponseReviewShop> dataResponseReviewShopResponse) {
                        return null;
                    }
                });
    }
}
