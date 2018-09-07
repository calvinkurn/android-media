package com.tokopedia.seller.logistic.data.repository;

import com.tokopedia.seller.logistic.model.CouriersModel;
import com.tokopedia.seller.logistic.data.source.LogisticDataSource;
import com.tokopedia.seller.logistic.domain.DistrictLogisticDataRepository;

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
    public Observable<CouriersModel> getAvailableCouriers(int districtCode) {
        return logisticDataSource.getAvailableCouriers(districtCode);
    }
}
