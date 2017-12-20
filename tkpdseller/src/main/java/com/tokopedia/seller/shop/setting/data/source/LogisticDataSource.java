package com.tokopedia.seller.shop.setting.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictLogisticDataCache;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictLogisticDataCloud;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;

import java.lang.reflect.Type;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/27/17.
 */

@ShopSettingScope
public class LogisticDataSource {
    private final DistrictLogisticDataCloud districLogisticDataCloud;
    private final DistrictLogisticDataCache districtLogisticDataCache;

    @Inject
    public LogisticDataSource(DistrictLogisticDataCloud districLogisticDataCloud,
                              DistrictLogisticDataCache districtLogisticDataCache) {
        this.districLogisticDataCloud = districLogisticDataCloud;
        this.districtLogisticDataCache = districtLogisticDataCache;
    }

    public Observable<OpenShopCouriersModel> getAvailableCouriers(final int districtCode) {
        return districtLogisticDataCache.getAvailableCouriers(districtCode)
                .flatMap(new Func1<OpenShopCouriersModel, Observable<OpenShopCouriersModel>>() {
            @Override
            public Observable<OpenShopCouriersModel> call(OpenShopCouriersModel openShopCouriersModel) {
                if (openShopCouriersModel == null) {
                    return districLogisticDataCloud.getAvailableCouriers(districtCode)
                            .doOnNext(new Action1<OpenShopCouriersModel>() {
                                @Override
                                public void call(OpenShopCouriersModel openShopCouriersModel) {
                                    districtLogisticDataCache.saveToDB(openShopCouriersModel, districtCode);
                                }
                            });
                } else {
                    return Observable.just(openShopCouriersModel);
                }
            };
        });
    }


}
