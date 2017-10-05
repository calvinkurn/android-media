package com.tokopedia.session.login.domain.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.login.domain.factory.LoginFactory;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class LoginRepositoryImpl implements LoginRepository {

    private final LoginFactory loginFactory;

    public LoginRepositoryImpl(LoginFactory loginFactory) {
        this.loginFactory = loginFactory;
    }

    @Override
    public Observable<MakeLoginDomainModel> makeLogin(TKPDMapParam<String, Object> parameters) {
        return loginFactory.createCloudLoginSource(parameters);
    }
}
