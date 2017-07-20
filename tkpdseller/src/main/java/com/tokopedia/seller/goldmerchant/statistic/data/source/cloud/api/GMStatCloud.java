package com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetProductTable;
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
    public static final String SORT_TYP = "sort_type";
    public static final String SORT_BY = "sort_by";
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "page_size";

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

    public Observable<Response<GetTransactionTable>> getTransactionTable(long startDate, long endDate,
                                                                         int page, int pageSize,
                                                                         @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        if (sortType > -1) {
            param.put(SORT_TYP, String.valueOf(sortType));
        }
        if (sortBy > -1) {
            param.put(SORT_BY, String.valueOf(sortBy));
        }
        if (page > -1) {
            param.put(PAGE, String.valueOf(page));
        }
        if (pageSize > -1) {
            param.put(PAGE_SIZE, String.valueOf(pageSize));
        }
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

    public Observable<Response<GetBuyerGraph>> getBuyerGraph(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        ;
        return gmStatApi.getBuyerGraph(shopId, param);
    }

    public Observable<Response<GetKeyword>> getKeywordModel(String categoryId) {
        return gmStatApi.getKeyword(categoryId, new HashMap<String, String>());
    }

    public Observable<Response<GetShopCategory>> getShopCategory(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getShopCategory(shopId, param).take(1);
    }

    public Observable<Response<GetProductTable>> getProductTable(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getProductTable(shopId, param);
    }

    public Observable<Response<GetBuyerTable>> getBuyerTable(long startDate, long endDate) {
        String shopId = sessionHandler.getShopID();
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getBuyerTable(shopId, param);
    }

    private Map<String, String> generateStartEndDateMap(long startDate, long endDate) {
        Map<String, String> param = new HashMap<>();
        if (startDate > 0) {
            param.put(S_DATE, getFormattedDate(startDate));
        } else {
            param.remove(S_DATE);
        }

        if (endDate > 0) {
            param.put(E_DATE, getFormattedDate(endDate));
        } else {
            param.remove(E_DATE);
        }
        return param;
    }

    public String getFormattedDate(long dateLong) {
        Date date = new Date(dateLong);
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", new Locale("in", "ID"));// "HH:mm:ss:SSS"
        return formatter.format(date);
    }
}
