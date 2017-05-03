package com.tokopedia.session.forgotpassword.interactor;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Alifa on 10/17/2016.
 */

public interface ForgotPasswordRetrofitInteractor {

    Observable<Response<TkpdResponse>> resetPassword(TKPDMapParam<String,String> email);
}
