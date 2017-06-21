package com.tokopedia.session.login.domain.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public interface LoginRepository {

    Observable<MakeLoginDomainModel> makeLogin(TKPDMapParam<String, Object> parameters);
}
