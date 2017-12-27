package com.tokopedia.session.changephonenumber.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.factory.ChangePhoneNumberFactory;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 27/12/17.
 */

public class ChangePhoneNumberRepositoryImpl implements ChangePhoneNumberRepository {
    private final ChangePhoneNumberFactory changePhoneNumberFactory;

    @Inject
    public ChangePhoneNumberRepositoryImpl(ChangePhoneNumberFactory changePhoneNumberFactory) {
        this.changePhoneNumberFactory = changePhoneNumberFactory;
    }

    @Override
    public Observable<WarningViewModel> getWarning(TKPDMapParam<String, Object> parameters) {
        return changePhoneNumberFactory
                .createCloudChangePhoneNumberWarningSource()
                .getWarning(parameters);
    }
}
