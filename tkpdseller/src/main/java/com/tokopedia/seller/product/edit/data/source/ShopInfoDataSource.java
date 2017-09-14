package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.edit.data.source.cache.ShopInfoCache;
import com.tokopedia.seller.product.edit.data.source.cloud.ShopInfoCloud;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

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

    public Observable<ShopModel> getShopInfo() {
        Observable<ShopModel> cacheShopModel = ShopInfoCache.getShopInfo();
        if (cacheShopModel == null) {
            return getShopInfoFromNetwork();
        }
        return cacheShopModel;
    }

    public Observable<ShopModel> getShopInfoFromNetwork() {
        return shopInfoCloud.getShopInfo()
                .map(mapper)
                .doOnNext(new Action1<ShopModel>() {
                    @Override
                    public void call(ShopModel shopModel) {
                        ShopInfoCache.saveShopInfoToCache(shopModel);
                    }
                });
    }

    public boolean clearCacheShopInfo() {
        return ShopInfoCache.clearShopInfoCache();
    }
}
