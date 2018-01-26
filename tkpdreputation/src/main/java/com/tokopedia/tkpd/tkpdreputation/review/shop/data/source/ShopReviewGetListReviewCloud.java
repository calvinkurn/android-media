package com.tokopedia.tkpd.tkpdreputation.review.shop.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReputationReviewApi;

import java.util.HashMap;

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


    public Observable<DataResponseReviewShop> getReviewShopList(HashMap<String, String> params) {
        return reputationReviewApi.getReviewShopList(params)
                .map(new GetData<DataResponse<DataResponseReviewShop>>())
                .map(new Func1<DataResponse<DataResponseReviewShop>, DataResponseReviewShop>() {
                    @Override
                    public DataResponseReviewShop call(DataResponse<DataResponseReviewShop> dataResponseReviewShopDataResponse) {
                        return dataResponseReviewShopDataResponse.getData();
                    }
                });
    }
}
