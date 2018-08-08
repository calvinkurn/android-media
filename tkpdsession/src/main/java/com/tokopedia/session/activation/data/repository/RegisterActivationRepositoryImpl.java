package com.tokopedia.session.activation.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.activation.data.ActivateUnicodeModel;
import com.tokopedia.session.activation.data.ChangeEmailModel;
import com.tokopedia.session.activation.data.ResendActivationModel;
import com.tokopedia.session.activation.data.factory.RegisterActivationFactory;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class RegisterActivationRepositoryImpl implements RegisterActivationRepository {

    private final RegisterActivationFactory registerActivationFactory;

    public RegisterActivationRepositoryImpl(RegisterActivationFactory registerActivationFactory) {

        this.registerActivationFactory = registerActivationFactory;
    }

    @Override
    public Observable<ResendActivationModel> resendActivation(TKPDMapParam<String, Object> parameters) {
        return registerActivationFactory.createCloudResendActivationDataStore()
                .resendActivation(parameters);
    }

    @Override
    public Observable<ActivateUnicodeModel> activateWithUnicode(TKPDMapParam<String, Object> parameters) {
        return registerActivationFactory.createCloudActivateWithUnicodeDataStore()
                .activateWithUnicode(parameters);
    }

    @Override
    public Observable<ChangeEmailModel> changeEmail(TKPDMapParam<String, Object> parameters) {
        return registerActivationFactory.createCloudChangeEmailDataStore()
                .changeEmail(parameters);
    }
}
