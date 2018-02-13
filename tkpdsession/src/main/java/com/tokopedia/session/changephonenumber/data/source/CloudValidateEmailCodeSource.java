package com.tokopedia.session.changephonenumber.data.source;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.mapper.ValidateEmailCodeMapper;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 04/01/18.
 */

public class CloudValidateEmailCodeSource {
    private final AccountsService accountsService;
    private final ValidateEmailCodeMapper validateEmailCodeMapper;

    @Inject
    public CloudValidateEmailCodeSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                        ValidateEmailCodeMapper validateEmailCodeMapper) {
        this.accountsService = accountsService;
        this.validateEmailCodeMapper = validateEmailCodeMapper;
    }

    public Observable<Boolean> validateEmailCode(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().validateEmailCode(params)
                .map(validateEmailCodeMapper);
    }
}
