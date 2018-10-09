package com.tokopedia.network.service;

import com.tokopedia.network.SessionUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by nisie on 12/27/17.
 */

public interface AccountsBasicApi {

    @FormUrlEncoded
    @POST(SessionUrl.PATH_GET_TOKEN)
    Observable<Response<String>> getToken(@FieldMap Map<String, Object> params);
}
