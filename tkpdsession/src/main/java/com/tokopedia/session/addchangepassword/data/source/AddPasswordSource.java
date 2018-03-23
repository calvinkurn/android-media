package com.tokopedia.session.addchangepassword.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangepassword.data.mapper.AddPasswordMapper;
import com.tokopedia.session.addchangepassword.view.viewmodel.AddPasswordViewModel;

import rx.Observable;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordSource {

    private final AccountsService accountsService;
    private AddPasswordMapper addPasswordMapper;

    public AddPasswordSource(AccountsService accountsService, AddPasswordMapper addPasswordMapper) {
        this.accountsService = accountsService;
        this.addPasswordMapper = addPasswordMapper;
    }

    public Observable<AddPasswordViewModel> addPassword(RequestParams params) {
        return accountsService.getApi().addPassword(params.getParameters()).map(addPasswordMapper);
    }
}
