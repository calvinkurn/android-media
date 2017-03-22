package com.tokopedia.seller.shop.setting.data.repository;

import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.domain.DistrictDataRepository;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictDataRepositoryImpl implements DistrictDataRepository {
    private final DistrictDataSource districtDataSource;

    public DistrictDataRepositoryImpl(DistrictDataSource districtDataSource) {
        this.districtDataSource = districtDataSource;
    }

    @Override
    public Observable<Boolean> fetchDistrictData() {
        return districtDataSource.fetchDistrictData();
    }

    @Override
    public Observable<RecomendationDistrictDomainModel>
    getRecommendationLocationDistrict(String stringTyped) {
        return districtDataSource.getRecommendationLocationDistrict(stringTyped);
    }
}
