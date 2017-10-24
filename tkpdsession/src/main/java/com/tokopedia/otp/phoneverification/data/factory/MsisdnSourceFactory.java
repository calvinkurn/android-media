package com.tokopedia.otp.phoneverification.data.factory;

import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.domain.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.domain.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.source.VerifyMsisdnSource;

/**
 * Created by nisie on 3/7/17.
 */

public class MsisdnSourceFactory {
    private final Context context;
    private final SessionHandler sessionHandler;
    private AccountsService accountsService;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;
    private ChangePhoneNumberMapper changePhoneNumberMapper;

    public MsisdnSourceFactory(Context context, AccountsService accountsService,
                               VerifyPhoneNumberMapper verifyPhoneNumberMapper,
                               ChangePhoneNumberMapper changePhoneNumberMapper) {
        this.context = context;
        this.verifyPhoneNumberMapper = verifyPhoneNumberMapper;
        this.changePhoneNumberMapper = changePhoneNumberMapper;
        this.sessionHandler = new SessionHandler(MainApplication.getAppContext());
        this.accountsService = accountsService;
    }

    public VerifyMsisdnSource createCloudVerifyMsisdnSource() {
        return new VerifyMsisdnSource(accountsService,
                verifyPhoneNumberMapper, sessionHandler);
    }

}
