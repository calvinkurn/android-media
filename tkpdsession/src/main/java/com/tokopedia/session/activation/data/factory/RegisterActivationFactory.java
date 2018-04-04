package com.tokopedia.session.activation.data.factory;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.activation.data.mapper.ActivateUnicodeMapper;
import com.tokopedia.session.activation.data.mapper.ChangeEmailMapper;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;
import com.tokopedia.session.activation.data.source.CloudActivateUnicodeSource;
import com.tokopedia.session.activation.data.source.CloudChangeEmailSource;
import com.tokopedia.session.activation.data.source.CloudResendActivationSource;

/**
 * Created by nisie on 4/17/17.
 */

public class RegisterActivationFactory {
    private Context context;
    private final AccountsService accountsService;
    private final AccountsService accountsServiceBearer;
    private ResendActivationMapper resendActivationMapper;
    private ActivateUnicodeMapper activateUnicodeMapper;
    private ChangeEmailMapper changeEmailMapper;

    public RegisterActivationFactory(Context context,
                                     AccountsService accountsService,
                                     AccountsService accountsServiceBearer,
                                     ResendActivationMapper resendActivationMapper,
                                     ActivateUnicodeMapper activateUnicodeMapper,
                                     ChangeEmailMapper changeEmailMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.accountsServiceBearer  = accountsServiceBearer;
        this.resendActivationMapper = resendActivationMapper;
        this.activateUnicodeMapper = activateUnicodeMapper;
        this.changeEmailMapper = changeEmailMapper;
    }

    public CloudResendActivationSource createCloudResendActivationDataStore() {
        return new CloudResendActivationSource(context, accountsService, resendActivationMapper);
    }

    public CloudActivateUnicodeSource createCloudActivateWithUnicodeDataStore() {
        return new CloudActivateUnicodeSource(context, accountsServiceBearer, activateUnicodeMapper);
    }

    public CloudChangeEmailSource createCloudChangeEmailDataStore() {
        return new CloudChangeEmailSource(context, accountsService, changeEmailMapper);
    }
}
