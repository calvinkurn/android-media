package com.tokopedia.session.changephonenumber.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.source.CloudGetWarningSource;
import com.tokopedia.session.changephonenumber.data.source.CloudSendEmailSource;
import com.tokopedia.session.changephonenumber.data.source.CloudValidateNumberSource;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 27/12/17.
 */

public class ChangePhoneNumberRepositoryImpl implements ChangePhoneNumberRepository {
    private final CloudGetWarningSource cloudGetWarningSource;
    private final CloudSendEmailSource cloudSendEmailSource;
    private final CloudValidateNumberSource cloudValidateNumberSource;

    @Inject
    public ChangePhoneNumberRepositoryImpl(CloudGetWarningSource cloudGetWarningSource,
                                           CloudSendEmailSource cloudSendEmailSource,
                                           CloudValidateNumberSource cloudValidateNumberSource) {
        this.cloudGetWarningSource = cloudGetWarningSource;
        this.cloudSendEmailSource = cloudSendEmailSource;
        this.cloudValidateNumberSource = cloudValidateNumberSource;
    }

    @Override
    public Observable<WarningViewModel> getWarning(TKPDMapParam<String, Object> parameters) {
        return cloudGetWarningSource
                .getWarning(parameters);
    }

    @Override
    public Observable<Boolean> sendEmailOTP(TKPDMapParam<String, Object> parameters) {
        return cloudSendEmailSource.sendEmail(parameters);
    }

    @Override
    public Observable<Boolean> validateNumber(TKPDMapParam<String, Object> parameters) {
        return cloudValidateNumberSource.validateNumber(parameters);
    }
}
