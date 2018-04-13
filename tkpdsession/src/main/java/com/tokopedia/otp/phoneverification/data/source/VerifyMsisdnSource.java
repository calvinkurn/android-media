package com.tokopedia.otp.phoneverification.data.source;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.domain.mapper.VerifyPhoneNumberMapper;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyMsisdnSource {
    private AccountsService accountsService;
    private VerifyPhoneNumberMapper verifyPhoneNumberMapper;

    public VerifyMsisdnSource(AccountsService accountsService,
                              VerifyPhoneNumberMapper verifyPhoneNumberMapper) {
        this.accountsService = accountsService;
        this.verifyPhoneNumberMapper = verifyPhoneNumberMapper;
    }

    public Observable<VerifyPhoneNumberDomain> verifyPhoneNumber(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .verifyPhoneNumber(params).map(verifyPhoneNumberMapper);
    }

}
