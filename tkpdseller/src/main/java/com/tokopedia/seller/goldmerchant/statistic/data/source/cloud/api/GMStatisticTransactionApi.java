package com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api;

import com.tokopedia.seller.goldmerchant.statistic.constant.StatisticConstant;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author normansyahputa on 7/6/17.
 */
public interface GMStatisticTransactionApi {
    @GET(StatisticConstant.GET_TRANSACTION_GRAPH_URL)
    Observable<Response<GetTransactionGraph>> getTransactionGraph(@Path("id") String id, @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_TRANSACTION_TABLE_URL)
    Observable<Response<GetTransactionTable>> getTransactionTable(@Path("id") String id, @QueryMap Map<String, String> params);
}
