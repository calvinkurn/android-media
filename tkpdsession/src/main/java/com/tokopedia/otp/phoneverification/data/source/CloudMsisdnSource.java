package com.tokopedia.otp.phoneverification.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class CloudMsisdnSource {
    private final Context context;
    private AccountsService accountsService;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;

    public CloudMsisdnSource(Context context, AccountsService accountsService,
                             VerifyPhoneNumberMapper verifyPhoneNumberMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.verifyPhoneNumberMapper = verifyPhoneNumberMapper;
    }

    public Observable<VerifyPhoneNumberModel> verifyPhoneNumberModel(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .verifyPhoneNumber(params).map(verifyPhoneNumberMapper);
    }

}
