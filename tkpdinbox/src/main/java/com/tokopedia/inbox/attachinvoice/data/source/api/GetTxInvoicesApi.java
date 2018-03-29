package com.tokopedia.inbox.attachinvoice.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicePostRequest;
import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicesResponseWrapper;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public interface GetTxInvoicesApi {
    @POST("invoice-list")
    Observable<Response<GetInvoicesResponseWrapper>> getTXOrderList(@QueryMap Map<String, String> params, @Body GetInvoicePostRequest body);
}
