package com.tokopedia.otp.phoneverification.data.factory;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.source.CloudMsisdnSource;

/**
 * Created by nisie on 3/7/17.
 */

public class MsisdnSourceFactory {
    private final Context context;
    private AccountsService accountsService;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;

    public MsisdnSourceFactory(Context context) {
        this.context = context;
        this.verifyPhoneNumberMapper = new VerifyPhoneNumberMapper();

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(context);
        bundle.putString(AccountsService.AUTH_KEY,
                "Bearer " + sessionHandler.getAccessToken(context));
        bundle.putBoolean(AccountsService.USING_BOTH_AUTHORIZATION,
                true);
        this.accountsService = new AccountsService(bundle);
    }

    public CloudMsisdnSource createCloudOtpSource() {
        return new CloudMsisdnSource(context, accountsService,
                verifyPhoneNumberMapper);
    }
}
