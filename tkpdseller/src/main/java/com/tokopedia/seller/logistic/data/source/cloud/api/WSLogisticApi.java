package com.tokopedia.seller.logistic.data.source.cloud.api;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.seller.logistic.model.CouriersModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface WSLogisticApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<OpenShopDistrictServiceModel>> fetchDistrictData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP + TkpdBaseURL.Shop.PATH_GET_OPEN_SHOP_FORM)
    Observable<Response<DataResponse<CouriersModel>>> getLogisticAvailable(@FieldMap Map<String, String> params);
}
