package com.tokopedia.gm.statistic.data.source.cache;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.gm.statistic.data.source.db.GMStatActionType;
import com.tokopedia.gm.statistic.data.source.db.GMStatDataBase;
import com.tokopedia.gm.statistic.data.source.db.GMStatDataBase_Table;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hendry on 7/14/2017.
 */

public class GMStatCache {
    public static final long DATE_MIN_THRES = 86400000000L;
    private static final long EXPIRED_TIME = 3600 * 24; // 24 HOUR
    public static final int ONE_DAY_SECOND = 86400;

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

    public Observable<GetBuyerGraph> getBuyerGraph(long startDate, long endDate) {
        return getObservable(GMStatActionType.BUYER, startDate, endDate, GetBuyerGraph.class);
    }

    public Observable<GetKeyword> getKeywordModel(long categoryId) {
        return getObservable(GMStatActionType.KEYWORD, categoryId, -1, GetKeyword.class);
    }

    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        return getObservable(GMStatActionType.SHOP_CAT, startDate, endDate, GetShopCategory.class);
    }

    public Observable<Boolean> saveGMStat(@GMStatActionType int action,
                                          long startDate, long endDate, String jsonData){
        try {
            GMStatDataBase gmStatDataBase = new GMStatDataBase();
            gmStatDataBase.setAction(action);
            gmStatDataBase.setStartDate(getNormalizedDate(startDate));
            gmStatDataBase.setEndDate(getNormalizedDate(endDate));
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
                                             @NonNull Class<T> responseObjectClass ){
        GMStatDataBase gmStatDataBase = retrieveGMStatDatabase(action, startDate, endDate);
        if (gmStatDataBase == null){
            return null;
        }
        // check if expired
        if ((System.currentTimeMillis()/1000L) - gmStatDataBase.getTimeStamp() > EXPIRED_TIME) {
            deleteGMStatRow(gmStatDataBase);
            return null;
        }
        T response = getObjectParse(gmStatDataBase.getData(), responseObjectClass);
        if (response == null) {
            deleteGMStatRow(gmStatDataBase);
            return null;
        }
        return Observable.just(response);
    }

    private GMStatDataBase retrieveGMStatDatabase (@GMStatActionType int action, long startDate, long endDate){
        return new Select()
                .from(GMStatDataBase.class)
                .where(GMStatDataBase_Table.action.is(action))
                .and(GMStatDataBase_Table.startDate.is(getNormalizedDate(startDate)))
                .and(GMStatDataBase_Table.endDate.is(getNormalizedDate(endDate)))
                .querySingle();
    }

    public long getNormalizedDate(long dateLong) {
        if (dateLong > DATE_MIN_THRES) { // we just want to normalize long if it is date
            long dateLongSecond = dateLong / 1000L;
            return dateLongSecond - (dateLongSecond % ONE_DAY_SECOND);
        }
        return dateLong;
    }

    public <T> T getObjectParse(String jsonString, @NonNull Class<T> responseObjectClass){
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonString, responseObjectClass);
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
