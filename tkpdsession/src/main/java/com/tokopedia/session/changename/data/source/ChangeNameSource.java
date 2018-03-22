package com.tokopedia.session.changename.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.changename.data.mapper.ChangeNameMapper;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;

import rx.Observable;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameSource {

    private final AccountsService accountsService;
    private ChangeNameMapper changeNameMapper;

    public ChangeNameSource(AccountsService accountsService, ChangeNameMapper changeNameMapper) {
        this.accountsService = accountsService;
        this.changeNameMapper = changeNameMapper;
    }

    public Observable<ChangeNameViewModel> changeName(RequestParams params) {
        return accountsService.getApi().changeName(params.getParameters()).map(changeNameMapper);
    }
}
