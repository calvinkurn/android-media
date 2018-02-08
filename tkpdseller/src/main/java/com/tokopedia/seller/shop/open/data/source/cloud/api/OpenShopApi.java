package com.tokopedia.seller.shop.open.data.source.cloud.api;

import com.tokopedia.seller.shop.open.data.model.response.ResponseOpenShopPicture;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;


public interface OpenShopApi {

    @FormUrlEncoded
    @POST()
    Observable<Response<ResponseOpenShopPicture>> openShopPicture(@Url String urlHelper, @FieldMap Map<String, String> params);
}
