package com.tokopedia.seller.product.etalase.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.etalase.data.source.cloud.model.AddEtalaseServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.myetalase.MyEtalaseListServiceModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface MyEtalaseApi {

    @GET(TkpdBaseURL.Shop.PATH_MY_SHOP_ETALASE + TkpdBaseURL.Shop.PATH_GET_SHOP_ETALASE)
    Observable<Response<MyEtalaseListServiceModel>> fetchMyEtalase(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_ACTION_MY_SHOP_ETALASE + TkpdBaseURL.Shop.PATH_EVENT_SHOP_ADD_ETALASE)
    Observable<Response<AddEtalaseServiceModel>> addNewEtalase(@FieldMap TKPDMapParam<String, String> stringStringTKPDMapParam);
}
