package com.tokopedia.seller.shopscore.data.source.disk;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.utils.ErrorCheck;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.product.model.shopscore.detail.ShopScoreDetailServiceModel;
import com.tokopedia.core.product.model.shopscore.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.core.var.TkpdCache;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/27/17.
 */
public class ShopScoreCache {
    private final GlobalCacheManager cacheManager;

    @Inject
    public ShopScoreCache(GlobalCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void storeShopScoreSummary(ShopScoreSummaryServiceModel serviceModel) {
        String stringData = CacheUtil.convertModelToString(
                serviceModel,
                new TypeToken<ShopScoreSummaryServiceModel>() {
                }.getType()
        );
        saveToCache(TkpdCache.Key.SHOP_SCORE_SUMMARY, stringData);
    }

    public void storeShopScoreDetail(ShopScoreDetailServiceModel serviceModel) {
        String stringData = CacheUtil.convertModelToString(
                serviceModel,
                new TypeToken<ShopScoreDetailServiceModel>() {
                }.getType()
        );
        saveToCache(TkpdCache.Key.SHOP_SCORE_DETAIL, stringData);
    }

    private void saveToCache(String key, String stringData) {
        cacheManager.setKey(key);
        cacheManager.setValue(stringData);
        cacheManager.setCacheDuration(3600);
        cacheManager.store();
    }

    public Observable<ShopScoreDetailServiceModel> getShopScoreDetail() {
        return getShopScoreDetailCache()
                .map(new ErrorCheck<ShopScoreDetailServiceModel>());
    }

    public Observable<ShopScoreSummaryServiceModel> getShopScoreSummary() {
        return getShopScoreSummaryCache()
                .map(new ErrorCheck<ShopScoreSummaryServiceModel>());
    }

    @NonNull
    private Observable<ShopScoreSummaryServiceModel> getShopScoreSummaryCache() {
        return Observable.just(true)
                .map(new Func1<Boolean, ShopScoreSummaryServiceModel>() {
                         @Override
                         public ShopScoreSummaryServiceModel call(Boolean aBoolean) {
                             return cacheManager.getConvertObjData(
                                     TkpdCache.Key.SHOP_SCORE_SUMMARY,
                                     ShopScoreSummaryServiceModel.class
                             );
                         }
                     }
                );
    }

    @NonNull
    private Observable<ShopScoreDetailServiceModel> getShopScoreDetailCache() {
        return Observable.just(true)
                .map(new Func1<Boolean, ShopScoreDetailServiceModel>() {
                         @Override
                         public ShopScoreDetailServiceModel call(Boolean aBoolean) {
                             return cacheManager.getConvertObjData(
                                     TkpdCache.Key.SHOP_SCORE_DETAIL,
                                     ShopScoreDetailServiceModel.class
                             );
                         }
                     }
                );
    }
}
