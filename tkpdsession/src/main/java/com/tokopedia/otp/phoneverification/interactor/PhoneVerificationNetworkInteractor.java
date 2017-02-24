package com.tokopedia.otp.phoneverification.interactor;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 2/22/17.
 */

public interface PhoneVerificationNetworkInteractor {

    Observable<Response<TkpdResponse>> requestOTP(String userId, TKPDMapParam<String, String> param);

    Observable<Response<TkpdResponse>> verifyOTP(TKPDMapParam<String, String> param);


}
