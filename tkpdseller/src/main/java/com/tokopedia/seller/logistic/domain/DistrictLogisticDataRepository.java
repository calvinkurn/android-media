package com.tokopedia.seller.logistic.domain;

import com.tokopedia.seller.logistic.model.CouriersModel;

import rx.Observable;

/**
 * @author by sebastianuskh on 3/20/17.
 */

public interface DistrictLogisticDataRepository {
    Observable<CouriersModel> getAvailableCouriers(int districtCode);
}
