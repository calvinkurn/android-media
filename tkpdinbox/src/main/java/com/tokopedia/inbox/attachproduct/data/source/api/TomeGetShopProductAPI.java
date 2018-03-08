package com.tokopedia.inbox.attachproduct.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendri on 02/03/18.
 */

public interface TomeGetShopProductAPI {
    @GET(TkpdBaseURL.Tome.PATH_GET_SHOP_PRODUCT)
    Observable<Response<TkpdResponse>> getShopProduct(@QueryMap Map<String, String> params);
}
