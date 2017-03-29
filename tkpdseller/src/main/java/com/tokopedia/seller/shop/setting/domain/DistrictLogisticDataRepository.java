package com.tokopedia.seller.shop.setting.domain;

import com.tokopedia.seller.shop.setting.domain.model.LogisticAvailableDomainModel;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import rx.Observable;

/**
 * @author by sebastianuskh on 3/20/17.
 */

public interface DistrictLogisticDataRepository {
    Observable<Boolean> fetchDistrictData();

    Observable<RecomendationDistrictDomainModel> getRecommendationLocationDistrict(String stringTyped);

    Observable<LogisticAvailableDomainModel> getLogisticAvailable(int districtCode);
}
