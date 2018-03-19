package com.tokopedia.network.service;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.network.SessionUrl;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi.HEADER_USER_ID;

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

    @GET(SessionUrl.PATH_DISCOVER_LOGIN)
    Observable<Response<TkpdResponse>> discoverLogin(@QueryMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.CREATE_PASSWORD)
    Observable<Response<TkpdResponse>> createPassword(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.RESET_PASSWORD)
    Observable<Response<TkpdResponse>> resetPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(SessionUrl.RESENT_ACTIVATION)
    Observable<Response<TkpdResponse>> resentActivation(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.OTP.REQUEST_OTP)
    Observable<Response<TkpdResponse>> requestOtp(@Header(HEADER_USER_ID) String userId,
                                                  @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.OTP.VALIDATE_OTP)
    Observable<Response<TkpdResponse>> validateOtp(@FieldMap TKPDMapParam<String, Object> param);

    @FormUrlEncoded
    @POST(SessionUrl.MSISDN.VERIFY_PHONE_NUMBER)
    Observable<Response<TkpdResponse>> verifyPhoneNumber(@FieldMap TKPDMapParam<String, Object> param);

    @FormUrlEncoded
    @POST(SessionUrl.Image.GET_UPLOAD_HOST)
    Observable<Response<TkpdResponse>> getUploadHost(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.Image.VALIDATE_SIZE)
    Observable<Response<TkpdResponse>> validateImage(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.Image.SUBMIT_DETAIL)
    Observable<Response<TkpdResponse>> submitImage(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.Ktp.CHECK_STATUS)
    Observable<Response<TkpdResponse>> checkStatusKtp(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.OTP.REQUEST_OTP_EMAIL)
    Observable<Response<TkpdResponse>> requestOtpToEmail(@Header(HEADER_USER_ID) String userId,
                                                         @FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.DO_REGISTER)
    Observable<Response<TkpdResponse>> registerEmail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.ACTIVATE_UNICODE)
    Observable<Response<String>> activateWithUnicode(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.CHANGE_EMAIL)
    Observable<Response<TkpdResponse>> changeEmail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.MSISDN.CHANGE_PHONE_NUMBER)
    Observable<Response<TkpdResponse>> changePhoneNumber(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.User.PATH_MAKE_LOGIN)
    Observable<Response<TkpdResponse>> makeLogin(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.User.PATH_MAKE_LOGIN)
    Call<String> makeLoginsynchronous(@FieldMap TKPDMapParam<String, Object> parameters);

    @GET(SessionUrl.PATH_GET_INFO)
    Observable<Response<String>> getUserInfo(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.PATH_EDIT_PROFILE)
    Observable<Response<TkpdResponse>> editProfile(@FieldMap TKPDMapParam<String, Object> parameters);

    @GET(SessionUrl.ChangeMSISDN.GET_WARNING)
    Observable<Response<TkpdResponse>> getWarning(@QueryMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.ChangeMSISDN.SEND_EMAIL)
    Observable<Response<TkpdResponse>> sendEmail(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.ChangeMSISDN.VALIDATE_EMAIL_CODE)
    Observable<Response<TkpdResponse>> validateEmailCode(@FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.ChangeMSISDN.VALIDATE)
    Observable<Response<TkpdResponse>> validateNumber(@FieldMap TKPDMapParam<String, Object> params);

    @GET(SessionUrl.OTP.PATH_GET_METHOD_LIST)
    Observable<Response<String>> getVerificationMethodList(@QueryMap TKPDMapParam<String,
            Object> parameters);


    @FormUrlEncoded
    @POST(SessionUrl.Register.PATH_REGISTER_MSISDN_CHECK)
    Observable<Response<TkpdResponse>> checkMsisdnRegisterPhoneNumber(@FieldMap TKPDMapParam<String, Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.OTP.REQUEST_OTP_REGISTER)
    Observable<Response<TkpdResponse>> requestRegisterOtp(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(SessionUrl.OTP.VERIFY_OTP_REGISTER)
    Observable<Response<TkpdResponse>> verifyRegisterOtp(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.Register.PATH_REGISTER_EMAIL_CHECK)
    Observable<Response<TkpdResponse>> checkEmail(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.UpdateProfile.PATH_ADD_EMAIL)
    Observable<Response<TkpdResponse>> addEmail(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(SessionUrl.Register.PATH_SEND_VERIFICATION_EMAIL)
    Observable<Response<TkpdResponse>> requestVerification(@FieldMap Map<String,
            Object> parameters);

}
