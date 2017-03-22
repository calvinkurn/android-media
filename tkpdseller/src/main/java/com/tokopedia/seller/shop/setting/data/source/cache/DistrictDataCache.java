package com.tokopedia.seller.shop.setting.data.source.cache;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.seller.shop.setting.data.source.cache.db.DistrictDataManager;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictDataCache {

    private final DistrictDataManager dataManager;

    @Inject
    public DistrictDataCache(DistrictDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Observable<Boolean> fetchDistrictData() {
        return null;
    }

    public Boolean storeDistrictData(OpenShopDistrictServiceModel serviceModel) {

        dataManager.storeDistrictData(serviceModel);

        return true;

    }



}
