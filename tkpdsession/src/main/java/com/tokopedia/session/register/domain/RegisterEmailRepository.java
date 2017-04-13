package com.tokopedia.session.register.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.RegisterEmailModel;

import rx.Observable;

/**
 * Created by nisie on 4/13/17.
 */

public interface RegisterEmailRepository {

    Observable<RegisterEmailModel> registerEmail(TKPDMapParam<String, Object> parameters);
}
