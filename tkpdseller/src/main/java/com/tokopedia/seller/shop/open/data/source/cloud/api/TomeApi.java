package com.tokopedia.seller.shop.open.data.source.cloud.api;

import com.tokopedia.seller.shop.constant.ShopOpenNetworkConstant;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCheckDomain;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseCheckShop;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseReserveDomain;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hendry on 3/22/2017.
 */

public interface TomeApi {

    @GET(ShopOpenNetworkConstant.PATH_DOMAIN_CHECK)
    Observable<Response<ResponseCheckDomain>> getDomainCheck(
            @Query(ShopOpenNetworkConstant.PARAM_SHOP_DOMAIN) String domainName);

    @GET(ShopOpenNetworkConstant.PATH_DOMAIN_CHECK)
    Observable<Response<ResponseCheckShop>> getShopCheck(
            @Query(ShopOpenNetworkConstant.PARAM_SHOP_NAME) String shopName);

    @FormUrlEncoded
    @POST(ShopOpenNetworkConstant.PATH_RESERVE_DOMAIN)
    Observable<Response<ResponseReserveDomain>> reserveDomain(
            @Field(ShopOpenNetworkConstant.PARAM_SHOP_NAME) String shopName,
            @Field(ShopOpenNetworkConstant.PARAM_SHOP_DOMAIN) String shopDomain);

    @GET(ShopOpenNetworkConstant.PATH_IS_RESERVE_DOMAIN)
    Observable<Response<ResponseIsReserveDomain>> isReserveDomain();

}