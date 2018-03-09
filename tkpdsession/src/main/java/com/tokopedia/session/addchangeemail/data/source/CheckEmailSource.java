package com.tokopedia.session.addchangeemail.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangeemail.data.mapper.CheckEmailMapper;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailSource {

    private final AccountsService accountsService;
    private CheckEmailMapper checkEmailMapper;

    public CheckEmailSource(AccountsService accountsService,
                            CheckEmailMapper checkEmailMapper) {
        this.accountsService = accountsService;
        this.checkEmailMapper = checkEmailMapper;
    }

    public Observable<CheckEmailViewModel> checkEmail(RequestParams params) {
        return accountsService.getApi().checkEmail(params.getParameters())
                .map(checkEmailMapper);
    }
}
