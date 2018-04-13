package com.tokopedia.session.register.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.model.RegisterEmailModel;
import com.tokopedia.session.register.data.factory.RegisterEmailSourceFactory;

import rx.Observable;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailRepositoryImpl implements RegisterEmailRepository {

    private final RegisterEmailSourceFactory registerEmailSourceFactory;

    public RegisterEmailRepositoryImpl(RegisterEmailSourceFactory registerEmailSourceFactory) {

        this.registerEmailSourceFactory = registerEmailSourceFactory;
    }

    @Override
    public Observable<RegisterEmailModel> registerEmail(TKPDMapParam<String, Object> parameters) {
        return registerEmailSourceFactory.createCloudRegisterEmailSource().registerEmail(parameters);
    }
}
