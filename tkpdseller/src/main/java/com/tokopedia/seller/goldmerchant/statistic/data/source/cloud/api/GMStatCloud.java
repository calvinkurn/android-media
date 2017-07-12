package com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatCloud {

    public static final String SHOP_ID = "shop_id";
    public static final String S_DATE = "sDate";
    public static final String E_DATE = "eDate";
    private GMStatApi gmStatApi;
    private SessionHandler sessionHandler;

    @Inject
    public GMStatCloud(GMStatApi gmStatApi, SessionHandler sessionHandler) {
        this.gmStatApi = gmStatApi;
        this.sessionHandler = sessionHandler;
    }

    public Observable<Response<GetTransactionGraph>> getTransactionGraph(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getTransactionGraph(shopId, param);
    }

    public Observable<Response<GetTransactionTable>> getTransactionTable(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getTransactionTable(shopId, param);
    }

    public Observable<Response<GetProductGraph>> getProductGraph(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getProductGraph(shopId, param);
    }

    public Observable<Response<GetPopularProduct>> getPopularProduct(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getPopularProduct(shopId, param);
    }

    public Observable<Response<GetBuyerData>> getBuyerGraph(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);;
        return gmStatApi.getBuyerGraph(shopId, param);
    }

    public Observable<Response<GetKeyword>> getKeywordModel(String categoryId) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = new HashMap<>();
        param.put(SHOP_ID, shopId);
        return gmStatApi.getKeyword(categoryId, param);
    }

    public Observable<Response<GetShopCategory>> getShopCategory(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getShopCategory(shopId, param).take(1);
    }

    private Map<String, String> generateStartEndDateMap(long startDate, long endDate){
        Map<String, String> param = new HashMap<>();
        param.put(S_DATE, getFormattedDate(startDate));
        param.put(E_DATE, getFormattedDate(endDate));
        return param;
    }

    public String getFormattedDate(long dateLong) {
        Date date = new Date(dateLong);
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", new Locale("in", "ID"));// "HH:mm:ss:SSS"
        return formatter.format(date);
    }
}
