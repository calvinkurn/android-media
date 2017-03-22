package com.tokopedia.seller.shop.setting.data.source.cache;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.database.model.DistrictDataDb;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.seller.shop.setting.data.source.cache.db.DistrictDataManager;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictDataCache {

    public static final String DISTRICT_DATA_CACHE = "DISTRICT_DATA_CACHE";
    public static final int CACHE_DURATION = 60 * 60 * 24;

    private final DistrictDataManager dataManager;
    private final LocalCacheHandler localCacheHandler;

    @Inject
    public DistrictDataCache(@ActivityContext Context context, DistrictDataManager dataManager) {
        this.dataManager = dataManager;
        this.localCacheHandler = new LocalCacheHandler(context, DISTRICT_DATA_CACHE);
    }

    public Observable<Boolean> fetchDistrictData() {
        return null;
    }

    public Boolean storeDistrictData(OpenShopDistrictServiceModel serviceModel) {

        dataManager.clearData();
        dataManager.storeDistrictData(serviceModel);
        localCacheHandler.setExpire(CACHE_DURATION);
        return true;

    }

    public Observable<Boolean> checkDistrictCache() {
        if (localCacheHandler.isExpired()) {
            throw new RuntimeException("Cache is expired");
        }
        if (dataManager.checkDataIsPresent()) {
            throw new RuntimeException("Cache is empty");
        }
        return Observable.just(true);
    }

    public Observable<List<DistrictDataDb>> getRecommendationLocationDistrict(String typedString) {
        return dataManager.getRecommendationDistrict(typedString);
    }
}
