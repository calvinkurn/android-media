package com.tokopedia.seller.shop.open.data.source;

import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.data.source.cloud.DistrictLogisticDataCloud;


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

    public Observable<OpenShopCouriersModel> getAvailableCouriers(final int districtCode) {
        // already got the district from Cache API
        return districLogisticDataCloud.getAvailableCouriers(districtCode);
    }


}
