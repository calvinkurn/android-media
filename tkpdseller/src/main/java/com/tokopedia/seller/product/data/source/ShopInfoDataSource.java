package com.tokopedia.seller.product.data.source;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.data.exception.NeedCheckVersionCategoryException;
import com.tokopedia.seller.product.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.data.source.cache.CategoryVersionCache;
import com.tokopedia.seller.product.data.source.cache.ShopInfoCache;
import com.tokopedia.seller.product.data.source.cloud.CategoryVersionCloud;
import com.tokopedia.seller.product.data.source.cloud.ShopInfoCloud;
import com.tokopedia.seller.product.data.source.cloud.model.CategoryVersionServiceModel;
import com.tokopedia.seller.product.data.source.db.CategoryDataManager;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.topads.data.model.data.Etalase;
import com.tokopedia.seller.topads.data.source.local.TopAdsEtalaseCacheDataSource;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopInfoDataSource {
    private final ShopInfoCloud shopInfoCloud;
    private final SimpleDataResponseMapper<ShopModel> mapper;

    @Inject
    public ShopInfoDataSource(ShopInfoCloud shopInfoCloud,
                              SimpleDataResponseMapper<ShopModel> mapper) {
        this.shopInfoCloud = shopInfoCloud;
        this.mapper = mapper;
    }

    public Observable<ShopModel> getShopInfo(String userId, String deviceId,
                                             String shopId, String shopDomain) {
        Observable<ShopModel> cacheShopModel = ShopInfoCache.getShopInfo();
        if (cacheShopModel == null) {
            return getShopInfoFromNetwork(userId, deviceId, shopId, shopDomain);
        }
        return cacheShopModel;
    }

    public Observable<ShopModel> getShopInfoFromNetwork(String userId, String deviceId,
                                             String shopId, String shopDomain) {
        return shopInfoCloud.getShopInfo(userId, deviceId, shopId, shopDomain)
                .map(mapper)
                .doOnNext(new Action1<ShopModel>() {
                    @Override
                    public void call(ShopModel shopModel) {
                        ShopInfoCache.saveShopInfoToCache(shopModel);
                    }
                });
    }


}
