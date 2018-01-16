package com.tokopedia.otp.data.factory;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.mapper.RequestOtpMapper;
import com.tokopedia.otp.data.mapper.ValidateOtpMapper;
import com.tokopedia.otp.data.source.CloudOtpSource;

/**
 * Created by nisie on 3/7/17.
 * @deprecated do not use.
 */

@Deprecated
public class OtpSourceFactory {
    private final Context context;
    private AccountsService accountsService;
    private RequestOtpMapper requestOtpMapper;
    private ValidateOtpMapper validateOtpMapper;


    public OtpSourceFactory(Context context) {
        this.context = context;
        this.requestOtpMapper = new RequestOtpMapper();
        this.validateOtpMapper = new ValidateOtpMapper();

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(context);
        bundle.putString(AccountsService.AUTH_KEY,
                String.format("Bearer %s", sessionHandler.getAccessToken(context)));

        this.accountsService = new AccountsService(bundle);
    }

    public CloudOtpSource createCloudOtpSource() {
        return new CloudOtpSource(context, accountsService,
                requestOtpMapper, validateOtpMapper);
    }

}
