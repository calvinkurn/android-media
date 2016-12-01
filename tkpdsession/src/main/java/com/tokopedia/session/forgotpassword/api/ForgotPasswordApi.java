package com.tokopedia.session.forgotpassword.api;

import com.tokopedia.core.session.model.network.ForgotPasswordData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by m.normansyah on 25/11/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface ForgotPasswordApi {
    @FormUrlEncoded
    @POST("/v4/action/general-usage/reset_password.pl")
    Observable<ForgotPasswordData> createForgotPassword(@Field("user_id") String userId,
                                                        @Field("device_id") String deviceId,
                                                        @Field("hash") String hash,
                                                        @Field("device_time") String deviceTime,
                                                        @Field("email") String email);
}
