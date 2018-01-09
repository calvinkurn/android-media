package com.tokopedia.seller.shop.open.data.source.cloud.api;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.logistic.model.CouriersModel;
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

/**
 * Created by nakama on 18/12/17.
 */

public interface OpenShopApi {

    @FormUrlEncoded
    @POST()
    Observable<Response<ResponseOpenShopPicture>> openShopPicture(@Url String urlHelper, @FieldMap Map<String, String> params);
}
