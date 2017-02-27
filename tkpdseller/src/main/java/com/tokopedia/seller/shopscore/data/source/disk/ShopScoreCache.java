package com.tokopedia.seller.shopscore.data.source.disk;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.data.source.cloud.model.summary.ShopScoreSummaryServiceModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/27/17.
 */
public class ShopScoreCache {
    private final GlobalCacheManager cacheManager;

    public ShopScoreCache(GlobalCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Observable<ShopScoreSummaryServiceModel> getShopScoreSummary() {
        return getShopScoreSummaryCache()
                .map(new ShopSummaryErrorCheck());
    }

    public void storeShopScoreSummary(ShopScoreSummaryServiceModel serviceModel) {
        cacheManager.setKey(TkpdCache.Key.SHOP_SCORE_SUMMARY);
        String stringData = CacheUtil.convertModelToString(
                serviceModel,
                new TypeToken<ShopScoreSummaryServiceModel>() {
                }.getType()
        );
        cacheManager.setValue(stringData);
        cacheManager.setCacheDuration(3600);
        cacheManager.store();
    }


    public void storeShopScoreDetail(ShopScoreDetailServiceModel serviceModel) {
        cacheManager.setKey(TkpdCache.Key.SHOP_SCORE_DETAIL);
        String stringData = CacheUtil.convertModelToString(
                serviceModel,
                new TypeToken<ShopScoreDetailServiceModel>() {
                }.getType()
        );
        cacheManager.setValue(stringData);
        cacheManager.setCacheDuration(3600);
        cacheManager.store();
    }

    public Observable<ShopScoreDetailServiceModel> getShopScoreDetail() {
        return getShopScoreDetailCache()
                .map(new ShopDetailErrorCheck());
    }

    @NonNull
    private Observable<ShopScoreSummaryServiceModel> getShopScoreSummaryCache() {
        return Observable.just(
                cacheManager.getConvertObjData(
                        TkpdCache.Key.SHOP_SCORE_SUMMARY,
                        ShopScoreSummaryServiceModel.class
                )
        );
    }

    @NonNull
    private Observable<ShopScoreDetailServiceModel> getShopScoreDetailCache() {
        return Observable.just(
                cacheManager.getConvertObjData(
                        TkpdCache.Key.SHOP_SCORE_DETAIL,
                        ShopScoreDetailServiceModel.class
                )
        );
    }

    private class ShopSummaryErrorCheck implements Func1<ShopScoreSummaryServiceModel, ShopScoreSummaryServiceModel> {
        @Override
        public ShopScoreSummaryServiceModel call(ShopScoreSummaryServiceModel serviceModel) {
            if (serviceModel == null) {
                throw new RuntimeException("Cache is empty");
            } else {
                return serviceModel;
            }
        }
    }

    private class ShopDetailErrorCheck implements Func1<ShopScoreDetailServiceModel, ShopScoreDetailServiceModel> {
        @Override
        public ShopScoreDetailServiceModel call(ShopScoreDetailServiceModel serviceModel) {
            if (serviceModel == null) {
                throw new RuntimeException("Cache is empty");
            } else {
                return serviceModel;
            }
        }
    }
}
