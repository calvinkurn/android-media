package com.tokopedia.session.activation.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.session.activation.data.mapper.ActivateUnicodeMapper;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;
import com.tokopedia.session.activation.data.source.CloudActivateUnicodeSource;
import com.tokopedia.session.activation.data.source.CloudResendActivationSource;

/**
 * Created by nisie on 4/17/17.
 */

public class RegisterActivationFactory {
    private Context context;
    private final AccountsService accountsService;
    private ResendActivationMapper resendActivationMapper;
    private ActivateUnicodeMapper activateUnicodeMapper;


    public RegisterActivationFactory(Context context,
                                     AccountsService accountsService,
                                     ResendActivationMapper resendActivationMapper,
                                     ActivateUnicodeMapper activateUnicodeMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.resendActivationMapper = resendActivationMapper;
        this.activateUnicodeMapper = activateUnicodeMapper;
    }

    public CloudResendActivationSource createCloudResendActivationDataStore() {
        return new CloudResendActivationSource(context, accountsService, resendActivationMapper);
    }

    public CloudActivateUnicodeSource createCloudActivateWithUnicodeDataStore() {
        return new CloudActivateUnicodeSource(context, accountsService, activateUnicodeMapper);
    }
}
