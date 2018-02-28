package com.tokopedia.session.register.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public interface RegisterPhoneNumberRepository {

    Observable<RegisterPhoneNumberModel> registerPhoneNumber(TKPDMapParam<String, Object> parameters);
}
