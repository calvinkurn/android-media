package com.tokopedia.network.service;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.SessionUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by yfsx on 19/03/18.
 */

public interface RegisterPhoneNumberApi {

    @FormUrlEncoded
    @POST(SessionUrl.Register.PATH_REGISTER_PHONE_NUMBER)
    Observable<Response<TkpdResponse>> registerPhoneNumber(@FieldMap Map<String, Object> params);

}
