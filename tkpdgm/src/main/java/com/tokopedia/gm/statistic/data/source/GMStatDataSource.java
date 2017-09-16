package com.tokopedia.gm.statistic.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.common.mapper.SimpleResponseMapper;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.common.williamchart.util.GoldMerchantDateUtils;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.data.source.cache.GMStatCache;
import com.tokopedia.gm.statistic.data.source.cloud.GMStatCloud;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetDateGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.gm.statistic.data.source.db.GMStatActionType;
import com.tokopedia.gm.statistic.view.model.GMDateRangeDateViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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

    public Observable<GetTransactionGraph> getTransactionGraph(String shopId, long startDate, long endDate) {
        Observable<GetTransactionGraph> getTransactionGraphObservable = gmStatCache.getTransactionGraph(startDate, endDate);
        if (getTransactionGraphObservable == null) {
            return gmStatCloud.getTransactionGraph(shopId, startDate, endDate)
                    .map(new SimpleResponseMapper<GetTransactionGraph>())
                    .doOnNext(getSaveAction(new TypeToken<GetTransactionGraph>() {}.getType(),
                            GMStatActionType.TRANS_GRAPH, startDate, endDate));
        } else {
            return getTransactionGraphObservable;
        }
    }

    public Observable<GetTransactionTable> getTransactionTable(String shopId, long startDate, long endDate,
                                                               int page, int pageSize,
                                                               @GMTransactionTableSortType final int sortType,
                                                               @GMTransactionTableSortBy final int sortBy) {
        // only cache this if it load all data.
        Observable<GetTransactionTable> getTransactionTableObservable = gmStatCache.getTransactionTable(startDate, endDate);
        if (getTransactionTableObservable == null) {
            getTransactionTableObservable = gmStatCloud.getTransactionTable(shopId, startDate, endDate,page, pageSize, sortType, sortBy)
                    .map(new SimpleResponseMapper<GetTransactionTable>());
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

    public Observable<GetProductGraph> getProductGraph(String shopId, long startDate, long endDate) {
        Observable<GetProductGraph> getProductGraphObservable = gmStatCache.getProductGraph(startDate, endDate);
        if (getProductGraphObservable == null) {
            return gmStatCloud.getProductGraph(shopId, startDate, endDate)
                    .map(new SimpleResponseMapper<GetProductGraph>())
                    .doOnNext(getSaveAction(new TypeToken<GetProductGraph>() {}.getType(),
                            GMStatActionType.PROD_GRAPH, startDate, endDate));
        } else {
            return getProductGraphObservable;
        }
    }

    public Observable<GetPopularProduct> getPopularProduct(String shopId, long startDate, long endDate) {
        Observable<GetPopularProduct> getProductGraphObservable = gmStatCache.getPopularProduct(startDate, endDate);
        if (getProductGraphObservable == null) {
            return gmStatCloud.getPopularProduct(shopId, startDate, endDate)
                    .map(new SimpleResponseMapper<GetPopularProduct>())
                    .doOnNext(getSaveAction(new TypeToken<GetPopularProduct>() {}.getType(),
                            GMStatActionType.POPULAR_PRODUCT, startDate, endDate));
        } else {
            return getProductGraphObservable;
        }
    }

    public Observable<GetBuyerGraph> getBuyerGraph(String shopId, long startDate, long endDate) {
        Observable<GetBuyerGraph> getBuyerDataObservable = gmStatCache.getBuyerGraph(startDate, endDate);
        if (getBuyerDataObservable == null) {
            return gmStatCloud.getBuyerGraph(shopId, startDate, endDate)
                    .map(new SimpleResponseMapper<GetBuyerGraph>())
                    .doOnNext(getSaveAction(new TypeToken<GetBuyerGraph>() {
                            }.getType(),
                            GMStatActionType.BUYER, startDate, endDate));
        } else {
            return getBuyerDataObservable;
        }
    }

    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        Observable<GetKeyword> getKeywordObservable = gmStatCache.getKeywordModel(Long.parseLong(categoryId));
        if (getKeywordObservable == null) {
            return gmStatCloud.getKeywordModel(categoryId)
                    .map(new SimpleResponseMapper<GetKeyword>())
                    .doOnNext(getSaveAction(new TypeToken<GetKeyword>() {}.getType(),
                            GMStatActionType.KEYWORD, Long.parseLong(categoryId), -1));
        } else {
            return getKeywordObservable;
        }
    }

    public Observable<GetShopCategory> getShopCategory(String shopId, long startDate, long endDate) {
        Observable<GetShopCategory> getShopCategoryObservable = gmStatCache.getShopCategory(startDate, endDate);
        if (getShopCategoryObservable == null) {
            return gmStatCloud.getShopCategory(shopId, startDate, endDate)
                    .map(new SimpleResponseMapper<GetShopCategory>())
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
                long startDateResponse = -1;
                long endDateResponse = -1;
                if (getObject instanceof GetDateGraph){
                    List<Integer> dateGraph = ((GetDateGraph) getObject).getDateGraph();
                    GMDateRangeDateViewModel gmDateRangeDateViewModel = getGmDateRangeDateViewModel(dateGraph);
                    startDateResponse = gmDateRangeDateViewModel.getStartDate();
                    endDateResponse = gmDateRangeDateViewModel.getEndDate();
                }

                String jsonString = CacheUtil.convertModelToString(getObject, type);
                if (jsonString == null){
                    return;
                }
                if (startDateResponse > 0){
                    gmStatCache.saveGMStat(action, startDateResponse, endDateResponse, jsonString);
                } else {
                    gmStatCache.saveGMStat(action, startDate, endDate, jsonString);
                }
            }
        };
    }

    protected GMDateRangeDateViewModel getGmDateRangeDateViewModel(List<Integer> dateGraph) {
        GMDateRangeDateViewModel gmDateRangeDateViewModel =
                new GMDateRangeDateViewModel();
        gmDateRangeDateViewModel.setStartDate(GoldMerchantDateUtils.getDateWithYear(dateGraph.get(0)));
        gmDateRangeDateViewModel.setEndDate(GoldMerchantDateUtils.getDateWithYear(dateGraph.get(dateGraph.size() - 1)));

        return gmDateRangeDateViewModel;
    }

    public Observable<GetProductTable> getProductTable(String shopId, long startDate, long endDate) {
        return gmStatCloud.getProductTable(shopId, startDate, endDate)
                .map(new SimpleResponseMapper<GetProductTable>());
    }

    public Observable<GetBuyerTable> getBuyerTable(String shopId, long startDate, long endDate) {
        return gmStatCloud.getBuyerTable(shopId, startDate, endDate)
                .map(new SimpleResponseMapper<GetBuyerTable>());
    }

}
