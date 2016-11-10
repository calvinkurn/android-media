package com.tokopedia.seller.selling.network.apiservices.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ricoharisin on 11/10/16.
 */

public interface ProductApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_GET_EDIT_PRODUCT_FORM)
    Observable<com.tokopedia.core.selling.orderReject.model.ResponseGetProductForm> getEditFormSelling(@FieldMap Map<String, String> params);
}
