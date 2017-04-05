package com.tokopedia.seller.product.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.data.source.cloud.model.myetalase.MyEtalaseListServiceModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface MyEtalaseApi {

    @GET(TkpdBaseURL.Shop.PATH_MY_SHOP_ETALASE + TkpdBaseURL.Shop.PATH_GET_SHOP_ETALASE)
    Observable<Response<MyEtalaseListServiceModel>> fetchMyEtalase(@QueryMap Map<String, String> params);
}
