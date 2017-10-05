package com.tokopedia.seller.reputation.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.reputation.data.mapper.ReputationReviewMapper;
import com.tokopedia.seller.reputation.data.source.ReputationReviewDataSource;
import com.tokopedia.seller.reputation.data.source.cloud.apiservice.api.SellerReputationApi;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.Map;

import rx.Observable;

import static com.tokopedia.seller.reputation.domain.interactor.ReviewReputationUseCase.RequestParamFactory.KEY_REVIEW_REPUTATION_PARAM;
import static com.tokopedia.seller.util.ShopNetworkController.RequestParamFactory.KEY_SHOP_ID;

/**
 * @author normansyahputa on 3/16/17.
 */

public class CloudReputationReviewDataSource implements ReputationReviewDataSource {

    private Context context;
    private SellerReputationApi sellerReputationApi;
    private ReputationReviewMapper reputationReviewMapper;

    public CloudReputationReviewDataSource(
            Context context,
            SellerReputationApi sellerReputationApi,
            ReputationReviewMapper reputationReviewMapper) {
        this.context = context;
        this.sellerReputationApi = sellerReputationApi;
        this.reputationReviewMapper = reputationReviewMapper;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return sellerReputationApi.getReputationHistory(shopId, param)
                .map(reputationReviewMapper);
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams) {
        return sellerReputationApi.getReputationHistory(
                requestParams.getString(KEY_SHOP_ID, ""),
                (Map<String, String>) requestParams.getObject(KEY_REVIEW_REPUTATION_PARAM))
                .map(reputationReviewMapper);
    }


}
