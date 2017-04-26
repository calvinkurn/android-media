package com.tokopedia.seller.product.data.source;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.data.source.cache.ShopInfoCache;
import com.tokopedia.seller.product.data.source.cloud.ShopInfoCloud;

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

    public Observable<ShopModel> getShopInfo(String shopId, String shopDomain) {
        Observable<ShopModel> cacheShopModel = ShopInfoCache.getShopInfo();
        if (cacheShopModel == null) {
            return getShopInfoFromNetwork(shopId, shopDomain);
        }
        return cacheShopModel;
    }

    public Observable<ShopModel> getShopInfoFromNetwork(String shopId, String shopDomain) {
        return shopInfoCloud.getShopInfo(shopId, shopDomain)
                .map(mapper)
                .doOnNext(new Action1<ShopModel>() {
                    @Override
                    public void call(ShopModel shopModel) {
                        ShopInfoCache.saveShopInfoToCache(shopModel);
                    }
                });
    }


}
