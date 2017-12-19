package com.tokopedia.seller.shop.setting.data.repository;

import com.tokopedia.seller.shop.open.data.model.OpenShopLogisticModel;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.data.source.LogisticDataSource;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictLogisticDataRepositoryImpl implements DistrictLogisticDataRepository {
    private final DistrictDataSource districtDataSource;
    private final LogisticDataSource logisticDataSource;

    public DistrictLogisticDataRepositoryImpl(DistrictDataSource districtDataSource, LogisticDataSource logisticDataSource) {
        this.districtDataSource = districtDataSource;
        this.logisticDataSource = logisticDataSource;
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

    @Override
    public Observable<OpenShopLogisticModel> getLogisticAvailable(int districtCode) {
        return logisticDataSource.getLogisticAvailable(districtCode);
    }
}
