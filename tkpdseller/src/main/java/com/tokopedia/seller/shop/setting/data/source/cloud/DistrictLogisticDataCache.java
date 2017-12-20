package com.tokopedia.seller.shop.setting.data.source.cloud;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.setting.data.datasource.cloud.OpenShopApi;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

import java.lang.reflect.Type;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/20/17.
 */

public class DistrictLogisticDataCache {
    private final GlobalCacheManager globalCacheManager;

    public static final String CACHE_KEY_DISTRICT_FOR_COURIERS = "CACHE_KEY_DISTRICT_FOR_COURIERS";
    public static final String CACHE_KEY_COURIERS = "CACHE_KEY_COURIERS";
    public static final int CACHE_DURATION = 86400; // 1 day

    @Inject
    public DistrictLogisticDataCache() {
        this.globalCacheManager = new GlobalCacheManager();
    }

    public Observable<OpenShopCouriersModel> getAvailableCouriers(int districtCode) {
        try {
            String cacheDistrictId = globalCacheManager.getValueString(CACHE_KEY_DISTRICT_FOR_COURIERS);
            if (cacheDistrictId.equals(String.valueOf(districtCode))) {
                OpenShopCouriersModel openShopCouriersModel =
                        globalCacheManager.getConvertObjData(CACHE_KEY_COURIERS, OpenShopCouriersModel.class);
                return Observable.just(openShopCouriersModel);
            } else {
                return Observable.just(null);
            }
        } catch (Exception e) {
            return Observable.just(null);
        }
    }

    public Observable<Boolean> saveToDB(OpenShopCouriersModel openShopCouriersModel,
                                                      int districtCode) {
        globalCacheManager.setKey(CACHE_KEY_DISTRICT_FOR_COURIERS);
        globalCacheManager.setValue(String.valueOf(districtCode));
        globalCacheManager.setCacheDuration(CACHE_DURATION);
        globalCacheManager.store();

        Type type = new TypeToken<OpenShopCouriersModel>() {}.getType();
        String jsonString = CacheUtil.convertModelToString(openShopCouriersModel, type);

        globalCacheManager.setKey(CACHE_KEY_COURIERS);
        globalCacheManager.setValue(jsonString);
        globalCacheManager.store();

        return Observable.just(true);
    }


}
