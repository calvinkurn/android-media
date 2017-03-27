package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.seller.shop.setting.data.mapper.LogisticDataDomainMapper;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictLogisticDataCloud;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.model.LogisticAvailableDomainModel;

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

    public Observable<LogisticAvailableDomainModel> getLogisticAvailable(int districtCode) {
        return districLogisticDataCloud.getLogisticAvailable(districtCode)
                .map(new LogisticDataDomainMapper());
    }
}
