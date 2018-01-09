package com.tokopedia.seller.logistic.data.source;

import com.tokopedia.seller.logistic.model.CouriersModel;
import com.tokopedia.seller.logistic.data.source.cloud.DistrictLogisticDataCloud;


import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/27/17.
 */

public class LogisticDataSource {
    private final DistrictLogisticDataCloud districLogisticDataCloud;

    @Inject
    public LogisticDataSource(DistrictLogisticDataCloud districLogisticDataCloud) {
        this.districLogisticDataCloud = districLogisticDataCloud;
    }

    public Observable<CouriersModel> getAvailableCouriers(final int districtCode) {
        // already got the district from Cache API
        return districLogisticDataCloud.getAvailableCouriers(districtCode);
    }


}
