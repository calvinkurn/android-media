package com.tokopedia.session.changephonenumber.data.factory;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.session.changephonenumber.data.mapper.GetWarningMapper;
import com.tokopedia.session.changephonenumber.data.source.CloudGetWarningSource;

import javax.inject.Inject;
import javax.inject.Named;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 27/12/17.
 */

//TODO remove this factory. Use data source directly
public class ChangePhoneNumberFactory {
    private final AccountsService accountsService;
    private final GetWarningMapper getWarningMapper;

    @Inject
    public ChangePhoneNumberFactory(@Named(BEARER_SERVICE) AccountsService accountsService, GetWarningMapper getWarningMapper) {
        this.accountsService = accountsService;
        this.getWarningMapper = getWarningMapper;
    }

    public CloudGetWarningSource createCloudChangePhoneNumberWarningSource() {
        return new CloudGetWarningSource(accountsService, getWarningMapper);
    }

}
