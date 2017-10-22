package com.tokopedia.seller.shop.setting.data.source.cache;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.seller.shop.setting.data.source.db.DistrictDataDb;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.seller.shop.setting.data.source.db.DistrictDataManager;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

@ShopSettingScope
public class DistrictDataCache {

    public static final String DISTRICT_DATA_CACHE = "DISTRICT_DATA_CACHE";
    public static final int CACHE_DURATION = 60 * 60 * 24;

    private final DistrictDataManager dataManager;
    private final LocalCacheHandler localCacheHandler;

    @Inject
    public DistrictDataCache(@ApplicationContext Context context, DistrictDataManager dataManager) {
        this.dataManager = dataManager;
        this.localCacheHandler = new LocalCacheHandler(context, DISTRICT_DATA_CACHE);
    }

    public Observable<Boolean> fetchDistrictData() {
        return null;
    }

    public Boolean storeDistrictData(OpenShopDistrictServiceModel serviceModel) {

        storeToDb(serviceModel);
        setCacheTimer();
        return true;

    }

    private void storeToDb(OpenShopDistrictServiceModel serviceModel) {
        dataManager.clearData();
        dataManager.storeDistrictData(serviceModel);
    }

    private void setCacheTimer() {
        localCacheHandler.setExpire(CACHE_DURATION);
    }

    public Observable<Boolean> checkDistrictCache() {
        if (localCacheHandler.isExpired()) {
            throw new RuntimeException("Cache is expired");
        }
        if (dataManager.checkDataIsEmpty()) {
            throw new RuntimeException("Cache is empty");
        }
        return Observable.just(true);
    }

    public Observable<List<DistrictDataDb>> getRecommendationLocationDistrict(String typedString) {
        return dataManager.getRecommendationDistrict(typedString);
    }
}
