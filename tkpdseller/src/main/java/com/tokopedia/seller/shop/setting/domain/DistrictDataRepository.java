package com.tokopedia.seller.shop.setting.domain;

import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public interface DistrictDataRepository {
    Observable<Boolean> fetchDistrictData();

    Observable<List<RecomendationDistrictDomainModel>> getRecommendationLocationDistrict(String stringTyped);
}
