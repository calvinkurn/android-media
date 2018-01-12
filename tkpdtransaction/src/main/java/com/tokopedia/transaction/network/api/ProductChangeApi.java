package com.tokopedia.transaction.network.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kris on 1/10/18. Tokopedia
 */

public interface ProductChangeApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_WEIGHT_PRICE)
    Observable<String> editWeightPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_DESCRIPTION)
    Observable<String> editDescription(@FieldMap Map<String, String> params);

}
