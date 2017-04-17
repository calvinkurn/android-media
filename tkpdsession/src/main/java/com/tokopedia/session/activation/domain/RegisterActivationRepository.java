package com.tokopedia.session.activation.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.activation.data.ResendActivationModel;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public interface RegisterActivationRepository {

    Observable<ResendActivationModel> resendActivation(TKPDMapParam<String, Object> parameters);

}
