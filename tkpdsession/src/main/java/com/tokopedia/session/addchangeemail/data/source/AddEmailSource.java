package com.tokopedia.session.addchangeemail.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangeemail.data.mapper.AddEmailMapper;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailSource {

    private final AccountsService accountsService;
    private AddEmailMapper addEmailMapper;

    public AddEmailSource(AccountsService accountsService,
                          AddEmailMapper addEmailMapper) {
        this.accountsService = accountsService;
        this.addEmailMapper = addEmailMapper;
    }

    public Observable<AddEmailViewModel> addEmail(RequestParams params) {
        return accountsService.getApi().addEmail(params.getParameters()).map(addEmailMapper);
    }
}
