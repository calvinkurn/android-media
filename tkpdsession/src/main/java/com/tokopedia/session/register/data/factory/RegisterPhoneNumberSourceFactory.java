package com.tokopedia.session.register.data.factory;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.register.data.mapper.RegisterPhoneNumberMapper;
import com.tokopedia.session.register.data.source.CloudRegisterPhoneNumberSource;

/**
 * @author by yfsx on 28/02/18.
 */

public class RegisterPhoneNumberSourceFactory {
    private Context context;
    private final AccountsService accountsService;
    private RegisterPhoneNumberMapper registerPhoneNumberMapper;

    public RegisterPhoneNumberSourceFactory(Context context,
                                            AccountsService accountsService,
                                            RegisterPhoneNumberMapper registerPhoneNumberMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.registerPhoneNumberMapper = registerPhoneNumberMapper;
    }

    public CloudRegisterPhoneNumberSource createCloudRegisterEmailSource() {
        return new CloudRegisterPhoneNumberSource(context, accountsService, registerPhoneNumberMapper);
    }


}
