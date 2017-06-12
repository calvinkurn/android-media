package com.tokopedia.otp.phoneverification.data.factory;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.data.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.source.CloudChangeMsisdnSource;
import com.tokopedia.otp.phoneverification.data.source.CloudVerifyMsisdnSource;

/**
 * Created by nisie on 3/7/17.
 */

public class MsisdnSourceFactory {
    private final Context context;
    private AccountsService accountsService;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;
    private ChangePhoneNumberMapper changePhoneNumberMapper;

    public MsisdnSourceFactory(Context context, AccountsService accountsService,
                               VerifyPhoneNumberMapper verifyPhoneNumberMapper,
                               ChangePhoneNumberMapper changePhoneNumberMapper) {
        this.context = context;
        this.verifyPhoneNumberMapper = verifyPhoneNumberMapper;
        this.changePhoneNumberMapper = changePhoneNumberMapper;

        this.accountsService = accountsService;
    }

    public CloudVerifyMsisdnSource createCloudVerifyMsisdnSource() {
        return new CloudVerifyMsisdnSource(context, accountsService,
                verifyPhoneNumberMapper);
    }

    public CloudChangeMsisdnSource createCloudChangeMsisdnSource() {
        return new CloudChangeMsisdnSource(context, accountsService,
                changePhoneNumberMapper);
    }
}
