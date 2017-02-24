package com.tokopedia.seller.topads.data.source.cloud.apiservice.api;

import com.tokopedia.seller.topads.domain.model.data.DataEtalase;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
public interface TopAdsShopApi {

//    @FormUrlEncoded
//    @POST("get_shop_info.pl")
//    Observable<Response<TkpdResponse>> getShopInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_shop_etalase.pl")
    Observable<Response<DataResponse<DataEtalase>>> getShopEtalase(@FieldMap Map<String, String> params);

//    @GET("get_shop_product.pl")
//    Observable<Response<TkpdResponse>> getShopProduct(@QueryMap Map<String, String> params);
//
//    @FormUrlEncoded
//    @POST("get_shop_talk.pl")
//    Observable<Response<TkpdResponse>> getShopTalk(@FieldMap Map<String, String> params);
//
//    @GET("get_shop_review.pl")
//    Observable<Response<TkpdResponse>> getShopReview(@QueryMap Map<String, String> params);
//
//    @GET("get_like_dislike_review_shop.pl")
//    Observable<Response<TkpdResponse>> getLikeDislike(@QueryMap Map<String, String> params);
//
//    @GET("get_people_who_favorite_myshop.pl")
//    Observable<Response<TkpdResponse>> getPeopleFavorite(@QueryMap TKPDMapParam<String, String> params);

}