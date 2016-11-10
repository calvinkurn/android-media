package com.tokopedia.seller.selling.network.apiservices.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditPrice;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ricoharisin on 11/10/16.
 */

public interface ProductActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_DESCRIPTION)
    Observable<ResponseEditDescription> editDescription(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_WEIGHT_PRICE)
    Observable<ResponseEditPrice> editWeightPrice(@FieldMap Map<String, String> params);
}
