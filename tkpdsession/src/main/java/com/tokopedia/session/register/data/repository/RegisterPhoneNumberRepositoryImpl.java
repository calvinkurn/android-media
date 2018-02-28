package com.tokopedia.session.register.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.factory.RegisterPhoneNumberSourceFactory;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class RegisterPhoneNumberRepositoryImpl implements RegisterPhoneNumberRepository {

    private final RegisterPhoneNumberSourceFactory registerPhoneNumberSourceFactory;

    public RegisterPhoneNumberRepositoryImpl(RegisterPhoneNumberSourceFactory registerPhoneNumberSourceFactory) {
        this.registerPhoneNumberSourceFactory = registerPhoneNumberSourceFactory;
    }

    @Override
    public Observable<RegisterPhoneNumberModel> registerPhoneNumber(TKPDMapParam<String, Object> parameters) {
        return registerPhoneNumberSourceFactory.createCloudRegisterEmailSource().registerPhoneNumber(parameters);
    }
}
