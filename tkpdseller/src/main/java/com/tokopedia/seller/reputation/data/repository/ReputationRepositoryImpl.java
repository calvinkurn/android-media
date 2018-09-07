package com.tokopedia.seller.reputation.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.domain.ReputationRepository;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class ReputationRepositoryImpl implements ReputationRepository {

    private CloudReputationReviewDataSource cloudReputationReviewDataSource;

    @Inject
    public ReputationRepositoryImpl(
            CloudReputationReviewDataSource cloudReputationReviewDataSource) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return cloudReputationReviewDataSource.getReputationHistory(shopId, param);
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams) {
        return cloudReputationReviewDataSource.getReputationHistory(requestParams);
    }
}
