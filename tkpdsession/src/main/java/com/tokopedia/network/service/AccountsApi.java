package com.tokopedia.network.service;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.network.SessionUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * AccountsApi
 * Created by stevenfredian on 5/25/16.
 */
public interface AccountsApi {

    @Deprecated
    @FormUrlEncoded
    @POST(SessionUrl.PATH_GET_TOKEN)
    Observable<Response<String>> getTokenOld(@FieldMap Map<String, String> params);

    @GET(SessionUrl.PATH_GET_INFO)
    Observable<Response<String>> getInfo(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(SessionUrl.User.PATH_MAKE_LOGIN)
    Observable<Response<TkpdResponse>> makeLogin(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(SessionUrl.CREATE_PASSWORD)
    Observable<Response<TkpdResponse>> createPassword(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.RESET_PASSWORD)
    Observable<Response<TkpdResponse>> resetPassword(@FieldMap Map<String, String> params);

    @GET(SessionUrl.PATH_GET_INFO)
    Observable<Response<String>> getUserInfo(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.PATH_EDIT_PROFILE)
    Observable<Response<TkpdResponse>> editProfile(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.UpdateProfile.PATH_CHANGE_NAME)
    Observable<Response<TkpdResponse>> changeName(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.UpdateProfile.PATH_ADD_PASSWORD)
    Observable<Response<TkpdResponse>> addPassword(@FieldMap Map<String,
            Object> parameters);
}
