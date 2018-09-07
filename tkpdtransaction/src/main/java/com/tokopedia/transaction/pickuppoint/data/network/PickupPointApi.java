package com.tokopedia.transaction.pickuppoint.data.network;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 15/01/18.
 */

public interface PickupPointApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_EDIT_PICKUP_POINT)
    Observable<Response<TkpdResponse>> editPickupPoint(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_REMOVE_PICKUP_POINT)
    Observable<Response<TkpdResponse>> removePickupPoint(@FieldMap TKPDMapParam<String, String> params);

}
