package com.tokopedia.seller.shop.open.data.source.cloud.api;

import com.tokopedia.seller.shop.open.constant.ShopOpenNetworkConstant;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCheckDomainName;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCheckShopName;
import com.tokopedia.seller.shop.open.data.model.response.ResponseCreateShop;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseSaveShopDesc;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.DataResponse;

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
    Observable<Response<DataResponse<ResponseCheckDomainName>>> getDomainCheck(
            @Query(ShopOpenNetworkConstant.PARAM_SHOP_DOMAIN) String domainName);

    @GET(ShopOpenNetworkConstant.PATH_DOMAIN_CHECK)
    Observable<Response<DataResponse<ResponseCheckShopName>>> getShopCheck(
            @Query(ShopOpenNetworkConstant.PARAM_SHOP_NAME) String shopName);

    @FormUrlEncoded
    @POST(ShopOpenNetworkConstant.PATH_RESERVE_DOMAIN)
    Observable<Response<DataResponse<ResponseReserveDomain>>> reserveDomain(
            @Field(ShopOpenNetworkConstant.PARAM_SHOP_NAME) String shopName,
            @Field(ShopOpenNetworkConstant.PARAM_SHOP_DOMAIN) String shopDomain);

    @GET(ShopOpenNetworkConstant.PATH_IS_RESERVE_DOMAIN)
    Observable<Response<DataResponse<ResponseIsReserveDomain>>> isReserveDomain();


    @FormUrlEncoded
    @POST(ShopOpenNetworkConstant.PATH_RESERVE_SHOP_DESC_INFO)
    Observable<Response<DataResponse<ResponseSaveShopDesc>>> reserveShopDescInfo(@FieldMap Map<String, String> params);

    @POST(ShopOpenNetworkConstant.PATH_CREATE_SHOP)
    Observable<Response<DataResponse<ResponseCreateShop>>> createShop();
}