package com.tokopedia.session.changephonenumber.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.session.changephonenumber.data.mapper.WarningMapper;
import com.tokopedia.session.changephonenumber.data.source.CloudChangePhoneNumberWarningSource;
import com.tokopedia.session.changephonenumber.data.source.CloudGetUploadHostSource;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 27/12/17.
 */

public class ChangePhoneNumberFactory {
    private final AccountsService accountsService;
    private final WarningMapper warningMapper;

    @Inject
    public ChangePhoneNumberFactory(@Named(BEARER_SERVICE) AccountsService accountsService, WarningMapper warningMapper) {
        this.accountsService = accountsService;
        this.warningMapper = warningMapper;
    }

    public CloudChangePhoneNumberWarningSource createCloudChangePhoneNumberWarningSource() {
        return new CloudChangePhoneNumberWarningSource(accountsService, warningMapper);
    }

}
