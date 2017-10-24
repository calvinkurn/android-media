package com.tokopedia.otp.data.factory;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.domain.mapper.OldRequestOtpMapper;
import com.tokopedia.otp.domain.mapper.OldValidateOtpMapper;
import com.tokopedia.otp.data.source.CloudOtpSource;

/**
 * Created by nisie on 3/7/17.
 */

public class OtpSourceFactory {
    private final Context context;
    private AccountsService accountsService;
    private OldRequestOtpMapper oldRequestOtpMapper;
    private OldValidateOtpMapper validateOtpMapper;


    public OtpSourceFactory(Context context) {
        this.context = context;
        this.oldRequestOtpMapper = new OldRequestOtpMapper();
        this.validateOtpMapper = new OldValidateOtpMapper();

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(context);
        bundle.putString(AccountsService.AUTH_KEY,
                String.format("Bearer %s", sessionHandler.getAccessToken(context)));

        this.accountsService = new AccountsService(bundle);
    }

    public CloudOtpSource createCloudOtpSource() {
        return new CloudOtpSource(context, accountsService,
                oldRequestOtpMapper, validateOtpMapper);
    }

}
