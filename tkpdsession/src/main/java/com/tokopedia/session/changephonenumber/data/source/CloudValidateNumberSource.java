package com.tokopedia.session.changephonenumber.data.source;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.mapper.ValidateNumberMapper;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 03/01/18.
 */

public class CloudValidateNumberSource {
    private final AccountsService accountsService;
    private final ValidateNumberMapper validateNumberMapper;

    @Inject
    public CloudValidateNumberSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                     ValidateNumberMapper validateNumberMapper) {
        this.accountsService = accountsService;
        this.validateNumberMapper = validateNumberMapper;
    }

    public Observable<Boolean> validateNumber(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().validateNumber(params)
                .map(validateNumberMapper);
    }
}
