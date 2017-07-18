package com.tokopedia.seller.goldmerchant.statistic.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cache.GMStatCache;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatCloud;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.data.source.db.GMStatActionType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

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
                    .map(new SimpleDataResponseMapper<GetTransactionGraph>())
                    .doOnNext(getSaveAction(new TypeToken<GetTransactionGraph>() {}.getType(),
                            GMStatActionType.TRANS_GRAPH, startDate, endDate));
        } else {
            return getTransactionGraphObservable;
        }
    }

    public Observable<GetTransactionTable> getTransactionTable(long startDate, long endDate,
                                                               int page, int pageSize,
                                                               @GMTransactionTableSortType final int sortType,
                                                               @GMTransactionTableSortBy final int sortBy) {
        // only cache this if it load all data.
        Observable<GetTransactionTable> getTransactionTableObservable = gmStatCache.getTransactionTable(startDate, endDate);
        if (getTransactionTableObservable == null) {
            getTransactionTableObservable = gmStatCloud.getTransactionTable(startDate, endDate,page, pageSize, sortType, sortBy)
                    .map(new SimpleDataResponseMapper<GetTransactionTable>());
            // if it load all data, save to cache, else, just remove the cache
            // never save cache when it load per page only.
            if (page == -1 && pageSize == -1) {
                getTransactionTableObservable.doOnNext(getSaveAction(new TypeToken<GetTransactionTable>() {
                        }.getType(),
                        GMStatActionType.TRANS_TABLE, startDate, endDate));
            } else {
                clearTransactionTableCache();
            }
            return getTransactionTableObservable;
        } else {
            // this cache is for performance, so we don't need to retrieve the data from server again. just sort in local.
            // If there is cache for the transaction table data, sort the observable based on the type and sortby
            if (sortType < 0){ // we have no sortType (asc, desc)
                return getTransactionTableObservable;
            }
            final int ascendingSign = (sortType == GMTransactionTableSortType.ASCENDING)? 1 : -1;
            return getTransactionTableObservable.map(new Func1<GetTransactionTable, GetTransactionTable>() {
                @Override
                public GetTransactionTable call(GetTransactionTable getTransactionTable) {
                    List<GetTransactionTable.Cell> sortedList = new ArrayList<>();
                    Collections.copy(sortedList, getTransactionTable.getCells());
                    Collections.sort(sortedList, new Comparator<GetTransactionTable.Cell>() {
                        @Override
                        public int compare(GetTransactionTable.Cell o1, GetTransactionTable.Cell o2) {
                            switch (sortBy) {
                                case GMTransactionTableSortBy.DELIVERED_AMT:
                                    return ascendingSign * (o1.getDeliveredAmt() - o2.getDeliveredAmt());
                                case GMTransactionTableSortBy.ORDER_SUM:
                                    return ascendingSign * (o1.getOrderSum() - o2.getOrderSum());
                                case GMTransactionTableSortBy.TRANS_SUM:
                                    return ascendingSign * (o1.getTransSum() - o2.getTransSum());
                                default:
                                    return 0;

                            }
                        }
                    });
                    getTransactionTable.setCells(sortedList);
                    return getTransactionTable;
                }
            });
        }
    }

    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        Observable<GetProductGraph> getProductGraphObservable = gmStatCache.getProductGraph(startDate, endDate);
        if (getProductGraphObservable == null) {
            return gmStatCloud.getProductGraph(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetProductGraph>())
                    .doOnNext(getSaveAction(new TypeToken<GetProductGraph>() {}.getType(),
                            GMStatActionType.PROD_GRAPH, startDate, endDate));
        } else {
            return getProductGraphObservable;
        }
    }

    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        Observable<GetPopularProduct> getProductGraphObservable = gmStatCache.getPopularProduct(startDate, endDate);
        if (getProductGraphObservable == null) {
            return gmStatCloud.getPopularProduct(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetPopularProduct>())
                    .doOnNext(getSaveAction(new TypeToken<GetPopularProduct>() {}.getType(),
                            GMStatActionType.POPULAR_PRODUCT, startDate, endDate));
        } else {
            return getProductGraphObservable;
        }
    }

    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        Observable<GetBuyerData> getBuyerDataObservable = gmStatCache.getBuyerGraph(startDate, endDate);
        if (getBuyerDataObservable == null) {
            return gmStatCloud.getBuyerGraph(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetBuyerData>())
                    .doOnNext(getSaveAction(new TypeToken<GetBuyerData>() {}.getType(),
                            GMStatActionType.BUYER, 0,0));
        } else {
            return getBuyerDataObservable;
        }
    }

    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        Observable<GetKeyword> getKeywordObservable = gmStatCache.getKeywordModel();
        if (getKeywordObservable == null) {
            return gmStatCloud.getKeywordModel(categoryId)
                    .map(new SimpleDataResponseMapper<GetKeyword>())
                    .doOnNext(getSaveAction(new TypeToken<GetKeyword>() {}.getType(),
                            GMStatActionType.KEYWORD, 0,0));
        } else {
            return getKeywordObservable;
        }
    }

    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        Observable<GetShopCategory> getShopCategoryObservable = gmStatCache.getShopCategory(startDate, endDate);
        if (getShopCategoryObservable == null) {
            return gmStatCloud.getShopCategory(startDate, endDate)
                    .map(new SimpleDataResponseMapper<GetShopCategory>())
                    .doOnNext(getSaveAction(new TypeToken<GetShopCategory>() {}.getType(),
                            GMStatActionType.SHOP_CAT, startDate, endDate));
        } else {
            return getShopCategoryObservable;
        }
    }

    public Observable<Boolean> clearAllCache(){
        return gmStatCache.clearAllCache();
    }

    public Observable<Boolean> clearTransactionTableCache(){
        return gmStatCache.clearTransactionTableCache();
    }

    public <T> Action1<T> getSaveAction(final Type type,
                                        @GMStatActionType final int action,
                                        final long startDate,
                                        final long endDate){
        return new Action1<T>() {
            @Override
            public void call(T getObject) {
                String jsonString = CacheUtil.convertModelToString(getObject, type);
                if (jsonString == null){
                    return;
                }
                gmStatCache.saveGMStat(action, startDate, endDate, jsonString);
            }
        };
    }
}
