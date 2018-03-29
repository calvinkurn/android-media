package com.tokopedia.transaction.checkout.data.service;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 22/03/18.
 */

public interface RatesApi {

    @GET(TkpdBaseURL.Shipment.PATH_RATES_V2)
    Observable<Response<String>> calculateShippingRate(
            @QueryMap TKPDMapParam<String, String> stringStringMap
    );

}
