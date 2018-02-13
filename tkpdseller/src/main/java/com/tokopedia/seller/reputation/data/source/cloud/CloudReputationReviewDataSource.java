package com.tokopedia.seller.reputation.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.seller.reputation.data.mapper.ReputationReviewMapper;
import com.tokopedia.seller.reputation.data.model.response.SellerReputationResponse;
import com.tokopedia.seller.reputation.data.source.ReputationReviewDataSource;
import com.tokopedia.seller.reputation.data.source.cloud.apiservice.api.ReputationApi;
import com.tokopedia.seller.reputation.data.source.cloud.apiservice.api.SellerReputationApi;
import com.tokopedia.seller.reputation.domain.interactor.SpeedReputationUseCase;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.seller.reputation.domain.interactor.ReviewReputationUseCase.RequestParamFactory.KEY_REVIEW_REPUTATION_PARAM;
import static com.tokopedia.seller.util.ShopNetworkController.RequestParamFactory.KEY_SHOP_ID;

/**
 * @author normansyahputa on 3/16/17.
 */

public class CloudReputationReviewDataSource implements ReputationReviewDataSource {

    private SellerReputationApi sellerReputationApi;
    private ReputationApi reputationApi;

    @Inject
    public CloudReputationReviewDataSource(
            SellerReputationApi sellerReputationApi,
            ReputationApi reputationApi) {
        this.sellerReputationApi = sellerReputationApi;
        this.reputationApi = reputationApi;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return sellerReputationApi.getReputationHistory(shopId, param)
                .map(new Func1<Response<SellerReputationResponse>, SellerReputationDomain>() {
                    @Override
                    public SellerReputationDomain call(Response<SellerReputationResponse> sellerReputationResponseResponse) {
                        return ReputationReviewMapper.getSellerReputationDomains(sellerReputationResponseResponse.body());
                    }
                });
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams) {
        return sellerReputationApi.getReputationHistory(
                requestParams.getString(KEY_SHOP_ID, ""),
                (Map<String, String>) requestParams.getObject(KEY_REVIEW_REPUTATION_PARAM))
                .map(new Func1<Response<SellerReputationResponse>, SellerReputationDomain>() {
                    @Override
                    public SellerReputationDomain call(Response<SellerReputationResponse> sellerReputationResponseResponse) {
                        return ReputationReviewMapper.getSellerReputationDomains(sellerReputationResponseResponse.body());
                    }
                });
    }

    @Override
    public Observable<SpeedReputation> getReputationSpeed(RequestParams requestParams) {
        return reputationApi.getReputationSpeed(
                requestParams.getString(SpeedReputationUseCase.KEY_SHOP_ID, ""))
                .map(new Func1<Response<SpeedReputation>, SpeedReputation>() {
                    @Override
                    public SpeedReputation call(Response<SpeedReputation> stringResponse) {
                        return stringResponse.body();
                    }
                });
    }


}
