package com.tokopedia.seller.shop.open.domain;

import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;

import rx.Observable;

/**
 * @author by sebastianuskh on 3/20/17.
 */

public interface DistrictLogisticDataRepository {
    Observable<OpenShopCouriersModel> getAvailableCouriers(int districtCode);
}
