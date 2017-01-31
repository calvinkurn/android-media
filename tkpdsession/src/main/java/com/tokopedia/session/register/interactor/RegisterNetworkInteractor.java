package com.tokopedia.session.register.interactor;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.model.RegisterViewModel;

import retrofit2.Response;
import rx.Observable;
import rx.Subscription;

/**
 * Created by nisie on 1/27/17.
 */

public interface RegisterNetworkInteractor {
    Observable<Response<TkpdResponse>> getValidateEmailObservable(TKPDMapParam<String, String> param);

    Observable<Integer>  smartRegister(TKPDMapParam<String, String> param);

    Observable<Response<TkpdResponse>>  finishRegister(TKPDMapParam<String, String> param);

    Observable<Response<TkpdResponse>>  resendActivation(TKPDMapParam<String, String> param);

}
