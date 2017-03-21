package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictModel;
import com.tokopedia.seller.shop.setting.data.source.cache.DistrictDataCache;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictDataCloud;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictDataSource {
    private final DistrictDataCache districtDataCache;
    private final DistrictDataCloud districtDataCloud;

    @Inject
    public DistrictDataSource(DistrictDataCache districtDataCache, DistrictDataCloud districtDataCloud) {
        this.districtDataCache = districtDataCache;
        this.districtDataCloud = districtDataCloud;
    }

    public Observable<Boolean> fetchDistrictData() {
        return districtDataCloud.fetchDistrictData()
                .map(new Func1<OpenShopDistrictModel, Boolean>() {
                    @Override
                    public Boolean call(OpenShopDistrictModel openShopDistrictModel) {
                        return true;
                    }
                });
    }
}
