package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.seller.shop.open.data.model.OpenShopLogisticModel;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictLogisticDataCloud;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/27/17.
 */

@ShopSettingScope
public class LogisticDataSource {
    private final DistrictLogisticDataCloud districLogisticDataCloud;

    @Inject
    public LogisticDataSource(DistrictLogisticDataCloud districLogisticDataCloud) {
        this.districLogisticDataCloud = districLogisticDataCloud;
    }

    public Observable<OpenShopLogisticModel> getLogisticAvailable(int districtCode) {
        return districLogisticDataCloud.getLogisticAvailable(districtCode);
    }
}
