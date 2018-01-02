package com.tokopedia.seller.shop.open.data.repository;

import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.data.source.LogisticDataSource;
import com.tokopedia.seller.shop.open.domain.DistrictLogisticDataRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictLogisticDataRepositoryImpl implements DistrictLogisticDataRepository {
    private final LogisticDataSource logisticDataSource;

    public DistrictLogisticDataRepositoryImpl(LogisticDataSource logisticDataSource) {
        this.logisticDataSource = logisticDataSource;
    }

    @Override
    public Observable<OpenShopCouriersModel> getAvailableCouriers(int districtCode) {
        return logisticDataSource.getAvailableCouriers(districtCode);
    }
}
