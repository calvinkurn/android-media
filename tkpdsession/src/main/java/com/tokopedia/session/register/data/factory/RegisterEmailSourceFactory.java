package com.tokopedia.session.register.data.factory;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.register.data.mapper.RegisterEmailMapper;
import com.tokopedia.session.register.data.source.CloudRegisterEmailSource;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailSourceFactory {

    private Context context;
    private final AccountsService accountsService;
    private RegisterEmailMapper registerEmailMapper;

    public RegisterEmailSourceFactory(Context context,
                                      AccountsService accountsService,
                                      RegisterEmailMapper registerEmailMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.registerEmailMapper = registerEmailMapper;
    }

    public CloudRegisterEmailSource createCloudRegisterEmailSource() {
        return new CloudRegisterEmailSource(context, accountsService, registerEmailMapper);
    }


}
