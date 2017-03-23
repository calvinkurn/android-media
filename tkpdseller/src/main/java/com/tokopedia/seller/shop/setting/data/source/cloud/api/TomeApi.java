package com.tokopedia.seller.shop.setting.data.source.cloud.api;

import com.tokopedia.seller.shop.constant.ShopNetworkConstant;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCheckDomain;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCheckShop;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseReserveDomain;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendry on 3/22/2017.
 */

public interface TomeApi {

    @GET(ShopNetworkConstant.PATH_DOMAIN_CHECK)
    Observable<Response<ResponseCheckDomain>> getDomainCheck(@QueryMap Map<String, String> params);

    @GET(ShopNetworkConstant.PATH_SHOP_CHECK)
    Observable<Response<ResponseCheckShop>> getShopCheck(@QueryMap Map<String, String> params);

    @POST(ShopNetworkConstant.PATH_RESERVE_DOMAIN)
    Observable<Response<ResponseReserveDomain>> reserveDomain(@QueryMap Map<String, String> params);

    @GET(ShopNetworkConstant.PATH_IS_RESERVE_DOMAIN)
    Observable<Response<ResponseIsReserveDomain>> isReserveDomain(@QueryMap Map<String, String> params);

}