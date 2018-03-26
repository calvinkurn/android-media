package com.tokopedia.inbox.attachinvoice.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public interface GetTxInvoicesApi {
    @GET(TkpdBaseURL.Transaction.PATH_GET_TX_ORDER_LIST)
    Observable<Response<TkpdResponse>> getTXOrderList(@QueryMap Map<String, String> params);
}
