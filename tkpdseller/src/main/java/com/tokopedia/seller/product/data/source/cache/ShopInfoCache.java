package com.tokopedia.seller.product.data.source.cache;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public class ShopInfoCache {
    private static final String SHOP_INFO_CACHE_NAME = "stuarts_shop_info";

    private static final int EXPIRED_TIME = 10800; // 3 hours

    public static void saveShopInfoToCache(ShopModel shopModel) {
        GlobalCacheManager manager = new GlobalCacheManager();
        String jsonString = CacheUtil.convertModelToString(
                shopModel,
                new TypeToken<ShopModel>() { }.getType());
        manager.setKey(SHOP_INFO_CACHE_NAME);
        manager.setValue(jsonString);
        manager.setCacheDuration(EXPIRED_TIME);
        manager.store();
    }

    public static Observable<ShopModel> getShopInfo() {
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        try {
            String jsonString = globalCacheManager.getValueString(SHOP_INFO_CACHE_NAME);
            if (TextUtils.isEmpty(jsonString)) {
                return null;
            }
            ShopModel shopModel= CacheUtil.convertStringToModel(jsonString,
                    new TypeToken<ShopModel>() {
                    }.getType());
            return Observable.just(shopModel);
        }
        catch (RuntimeException e) { // might be expired
            return null;
        }
    }

    public static Boolean clearShopInfoCache() {
        try {
            GlobalCacheManager manager = new GlobalCacheManager();
            manager.delete(SHOP_INFO_CACHE_NAME);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
