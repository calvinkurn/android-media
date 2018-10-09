package com.tokopedia.session.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.register.data.mapper.CreatePasswordMapper;
import com.tokopedia.session.register.domain.model.CreatePasswordDomain;

import rx.Observable;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordDataSource {
    private final AccountsService accountsService;
    private final CreatePasswordMapper createPasswordMapper;

    public CreatePasswordDataSource(AccountsService accountsService,
                                    CreatePasswordMapper createPasswordMapper) {
        this.accountsService = accountsService;
        this.createPasswordMapper = createPasswordMapper;
    }

    public Observable<CreatePasswordDomain> createPassword(RequestParams params) {
        return accountsService.getApi()
                .createPassword(params.getParameters())
                .map(createPasswordMapper);
    }
}
