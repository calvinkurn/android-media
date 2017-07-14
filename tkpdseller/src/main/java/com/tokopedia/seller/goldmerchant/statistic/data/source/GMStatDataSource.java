package com.tokopedia.seller.goldmerchant.statistic.data.source;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cache.GMStatCache;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatCloud;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class GMStatDataSource {

    private GMStatCloud gmStatCloud;
    private GMStatCache gmStatCache;

    @Inject
    public GMStatDataSource(
            GMStatCloud gmStatCloud, GMStatCache gmStatCache) {
        this.gmStatCloud = gmStatCloud;
        this.gmStatCache = gmStatCache;
    }

    public Observable<GetTransactionGraph> getTransactionGraph(long startDate, long endDate) {
        Observable<GetTransactionGraph> getTransactionGraphObservable = gmStatCache.getTransactionGraph(startDate, endDate);
        if (getTransactionGraphObservable == null) {
            return gmStatCloud.getTransactionGraph(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetTransactionGraph>());
        } else {
            return getTransactionGraphObservable;
        }
    }

    public Observable<GetTransactionTable> getTransactionTable(long startDate, long endDate) {
        Observable<GetTransactionTable> getTransactionTableObservable = gmStatCache.getTransactionTable(startDate, endDate);
        if (getTransactionTableObservable == null) {
            return gmStatCloud.getTransactionTable(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetTransactionTable>());
        } else {
            return getTransactionTableObservable;
        }
    }

    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        Observable<GetProductGraph> getProductGraphObservable = gmStatCache.getProductGraph(startDate, endDate);
        if (getProductGraphObservable == null) {
            return gmStatCloud.getProductGraph(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetProductGraph>());
        } else {
            return getProductGraphObservable;
        }
    }

    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        Observable<GetPopularProduct> getProductGraphObservable = gmStatCache.getPopularProduct(startDate, endDate);
        if (getProductGraphObservable == null) {
            return gmStatCloud.getPopularProduct(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetPopularProduct>());
        } else {
            return getProductGraphObservable;
        }
    }

    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        Observable<GetBuyerData> getBuyerDataObservable = gmStatCache.getBuyerGraph(startDate, endDate);
        if (getBuyerDataObservable == null) {
            return gmStatCloud.getBuyerGraph(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetBuyerData>());
        } else {
            return getBuyerDataObservable;
        }
    }

    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        Observable<GetKeyword> getKeywordObservable = gmStatCache.getKeywordModel();
        if (getKeywordObservable == null) {
            return gmStatCloud.getKeywordModel(categoryId)
                    .map(new SimpleDataResponseMapper<GetKeyword>());
        } else {
            return getKeywordObservable;
        }
    }

    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        Observable<GetShopCategory> getShopCategoryObservable = gmStatCache.getShopCategory(startDate, endDate);
        if (getShopCategoryObservable == null) {
            return gmStatCloud.getShopCategory(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetShopCategory>())
                    .doOnNext(new Action1<GetShopCategory>() {
                        @Override
                        public void call(GetShopCategory getShopCategory) {
                            CacheUtil.convertModelToString(getShopCategory,
                                    new TypeToken<GetShopCategory>() {
                                    }.getType());
                        }
                    });
        } else {
            return getShopCategoryObservable;
        }
    }

    public <T> Action1<T> getSaveAction(@NonNull Class<T> tClass){
        return new Action1<T>() {
            @Override
            public void call(T getObject) {
                
            }
        };
    }
}
