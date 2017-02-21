package com.tokopedia.session.activation.interactor;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 2/1/17.
 */

public interface ActivationNetworkInteractor {

    Observable<Response<TkpdResponse>> resendActivation(TKPDMapParam<String, String> param);


}
