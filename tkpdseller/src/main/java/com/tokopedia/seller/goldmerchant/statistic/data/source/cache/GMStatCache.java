package com.tokopedia.seller.goldmerchant.statistic.data.source.cache;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.data.source.db.GMStatActionType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.db.GMStatDataBase;
import com.tokopedia.seller.goldmerchant.statistic.data.source.db.GMStatDataBase_Table;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hendry on 7/14/2017.
 */

public class GMStatCache {
    private static final long EXPIRED_TIME = 3600; // 1 HOUR

    @Inject
    public GMStatCache() {

    }

    public Observable<GetTransactionGraph> getTransactionGraph(long startDate, long endDate) {
        return getObservable(GMStatActionType.TRANS_GRAPH, startDate, endDate, GetTransactionGraph.class);
    }

    public Observable<GetTransactionTable> getTransactionTable(long startDate, long endDate) {
        return getObservable(GMStatActionType.TRANS_TABLE, startDate, endDate, GetTransactionTable.class);
    }

    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        return getObservable(GMStatActionType.PROD_GRAPH, startDate, endDate, GetProductGraph.class);
    }

    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        return getObservable(GMStatActionType.POPULAR_PRODUCT, startDate, endDate, GetPopularProduct.class);
    }

    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        return getObservable(GMStatActionType.BUYER, startDate, endDate, GetBuyerData.class);
    }

    public Observable<GetKeyword> getKeywordModel() {
        return getObservable(GMStatActionType.KEYWORD, 0, 0, GetKeyword.class);
    }

    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        return getObservable(GMStatActionType.SHOP_CAT, startDate, endDate, GetShopCategory.class);
    }


    public Observable<Boolean> saveGMStat(@GMStatActionType int action,
                                          long startDate, long endDate, String jsonData){
        try {
            GMStatDataBase gmStatDataBase = new GMStatDataBase();
            gmStatDataBase.setAction(action);
            gmStatDataBase.setStartDate(startDate);
            gmStatDataBase.setEndDate(endDate);
            gmStatDataBase.setData(jsonData);
            gmStatDataBase.setTimeStamp(System.currentTimeMillis() / 1000L);
            gmStatDataBase.save();
            return Observable.just(true);
        }
        catch (Exception e) {
            return Observable.just(false);
        }
    }

    private <T> Observable<T> getObservable (@GMStatActionType int action,
                                             long startDate,
                                             long endDate,
                                             @NonNull Class<T> responseObjectErrorClass ){
        GMStatDataBase gmStatDataBase = retrieveGMStatDatabase(action, startDate, endDate);
        if (gmStatDataBase == null){
            return null;
        }
        // check if expired
        if ((System.currentTimeMillis()/1000L) - gmStatDataBase.getTimeStamp() > EXPIRED_TIME) {
            deleteGMStatRow(gmStatDataBase);
            return null;
        }
        T response = getObjectParse(gmStatDataBase.getData(), responseObjectErrorClass);
        if (response == null) {
            return null;
        }
        return Observable.just(response);
    }

    private GMStatDataBase retrieveGMStatDatabase (@GMStatActionType int action, long startDate, long endDate){
        return new Select()
                .from(GMStatDataBase.class)
                .where(GMStatDataBase_Table.action.is(action))
                .and(GMStatDataBase_Table.startDate.is(startDate))
                .and(GMStatDataBase_Table.endDate.is(endDate))
                .querySingle();
    }

    public <T> T getObjectParse(String jsonString, @NonNull Class<T> responseObjectErrorClass){
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonString, responseObjectErrorClass);
        } catch (JsonSyntaxException e) { // the json might not be the instance, so just return it
            return null;
        }
    }

    public Observable<Boolean> deleteGMStatRow(GMStatDataBase gmStatDataBaseRow) {
        if (gmStatDataBaseRow != null) {
            gmStatDataBaseRow.delete();
            return Observable.just(true);
        }
        return Observable.just(false);
    }

    public Observable<Boolean> clearAllCache(){
        new Delete().from(GMStatDataBase.class).execute();
        return Observable.just(true);
    }

    public Observable<Boolean> clearTransactionTableCache(){
        new Delete().from(GMStatDataBase.class)
                .where(GMStatDataBase_Table.action.is(GMStatActionType.TRANS_TABLE))
                .execute();
        return Observable.just(true);
    }

}
