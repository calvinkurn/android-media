package com.tokopedia.reputation.common.domain.repository;

import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ReputationCommonRepository {

    Observable<ReputationSpeed> getStatisticSpeed(String shopId);

}