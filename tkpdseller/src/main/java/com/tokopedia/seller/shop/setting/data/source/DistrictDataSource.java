package com.tokopedia.seller.shop.setting.data.source;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.seller.shop.setting.data.mapper.DistrictDataDomainMapper;
import com.tokopedia.seller.shop.setting.data.source.cache.DistrictDataCache;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictDataCloud;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import java.util.List;

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
        return Observable.just(true)
                .flatMap(new CheckDistrictCache())
                .onErrorResumeNext(fetchDistrictDataFromNetwork());
    }

    private class CheckDistrictCache implements Func1<Boolean, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(Boolean aBoolean) {
            return districtDataCache
                    .checkDistrictCache();
        }
    }

    private Observable<Boolean> fetchDistrictDataFromNetwork() {
        return districtDataCloud.fetchDistrictData()
                .map(new StoreDisctrictToCache());
    }

    private class StoreDisctrictToCache implements Func1<OpenShopDistrictServiceModel, Boolean> {
        @Override
        public Boolean call(OpenShopDistrictServiceModel openShopDistrictServiceModel) {
            return districtDataCache.storeDistrictData(openShopDistrictServiceModel);
        }
    }

    public Observable<RecomendationDistrictDomainModel> getRecommendationLocationDistrict(
            String stringTyped
    ) {
        return districtDataCache
                .getRecommendationLocationDistrict(stringTyped)
                .map(new DistrictDataDomainMapper(stringTyped));
    }



}
