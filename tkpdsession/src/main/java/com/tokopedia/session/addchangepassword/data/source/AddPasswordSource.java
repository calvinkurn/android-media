package com.tokopedia.session.addchangepassword.data.source;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangepassword.data.mapper.AddPasswordMapper;
import com.tokopedia.session.addchangepassword.view.viewmodel.AddPasswordViewModel;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordSource {

    private final AccountsService accountsService;
    private AddPasswordMapper addPasswordMapper;
    private SessionHandler sessionHandler;

    public AddPasswordSource(AccountsService accountsService,
                             AddPasswordMapper addPasswordMapper,
                             SessionHandler sessionHandler) {
        this.accountsService = accountsService;
        this.addPasswordMapper = addPasswordMapper;

        this.sessionHandler = sessionHandler;
    }

    public Observable<AddPasswordViewModel> addPassword(RequestParams params) {
        return accountsService.getApi()
                .addPassword(params.getParameters())
                .map(addPasswordMapper)
                .doOnNext(updateSessionPassword());
    }

    public Action1<AddPasswordViewModel> updateSessionPassword() {
        return new Action1<AddPasswordViewModel>() {
            @Override
            public void call(AddPasswordViewModel addPasswordViewModel) {
                sessionHandler.setHasPassword(true);
            }
        };
    }
}
