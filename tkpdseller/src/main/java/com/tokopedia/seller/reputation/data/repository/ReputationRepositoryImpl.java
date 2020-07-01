package com.tokopedia.seller.reputation.data.repository;

import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.domain.ReputationRepository;

import javax.inject.Inject;

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
}
