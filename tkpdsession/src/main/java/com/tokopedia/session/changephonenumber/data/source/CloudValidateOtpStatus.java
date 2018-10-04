package com.tokopedia.session.changephonenumber.data.source;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.changephonenumber.data.mapper.ValidateOtpStatusMapper;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * @author by alvinatin on 11/05/18.
 */

public class CloudValidateOtpStatus {
    private final AccountsService accountsService;
    private final ValidateOtpStatusMapper mapper;

    @Inject
    public CloudValidateOtpStatus(@Named(BEARER_SERVICE) AccountsService accountsService,
                                  ValidateOtpStatusMapper mapper) {
        this.accountsService = accountsService;
        this.mapper = mapper;
    }

    public Observable<Boolean> validateOtpStatus(Map<String, Object> param) {
        return accountsService.getApi().validateOtpStatus(param).map(mapper);
    }
}
