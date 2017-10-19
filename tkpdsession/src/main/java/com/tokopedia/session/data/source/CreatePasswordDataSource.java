package com.tokopedia.session.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.session.register.data.mapper.CreatePasswordMapper;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

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

    public Observable<CreatePasswordViewModel> createPassword(RequestParams params) {
        return accountsService.getApi()
                .createPassword(params.getParameters())
                .map(createPasswordMapper);
    }
}
