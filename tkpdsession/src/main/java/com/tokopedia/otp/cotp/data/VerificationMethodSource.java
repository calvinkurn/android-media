package com.tokopedia.otp.cotp.data;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.di.SessionModule;
import com.tokopedia.otp.cotp.domain.mapper.VerificationMethodMapper;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

/**
 * @author by nisie on 1/18/18.
 */

public class VerificationMethodSource {

    AccountsService accountsService;
    VerificationMethodMapper verificationMethodMapper;

    @Inject
    public VerificationMethodSource(@Named(SessionModule.HMAC_SERVICE) AccountsService accountsService,
                                    VerificationMethodMapper verificationMethodMapper) {
        this.accountsService = accountsService;
        this.verificationMethodMapper = verificationMethodMapper;
    }

    public Observable<ListVerificationMethod> getMethodList(RequestParams requestParams) {
        return accountsService.getApi()
                .getVerificationMethodList(requestParams.getParameters())
                .map(verificationMethodMapper);
    }
}
