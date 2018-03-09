package com.tokopedia.session.addchangeemail.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangeemail.data.mapper.AddEmailMapper;
import com.tokopedia.session.addchangeemail.data.mapper.CheckEmailMapper;
import com.tokopedia.session.addchangeemail.data.mapper.RequestVerificationMapper;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;
import com.tokopedia.session.addchangeemail.view.viewmodel.RequestVerificationViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailSource {

    private final AccountsService accountsService;
    private AddEmailMapper addEmailMapper;
    private CheckEmailMapper checkEmailMapper;
    private RequestVerificationMapper requestVerificationMapper;

    public AddEmailSource(AccountsService accountsService,
                          AddEmailMapper addEmailMapper,
                          CheckEmailMapper checkEmailMapper,
                          RequestVerificationMapper requestVerificationMapper) {
        this.accountsService = accountsService;
        this.addEmailMapper = addEmailMapper;
        this.checkEmailMapper = checkEmailMapper;
        this.requestVerificationMapper = requestVerificationMapper;
    }

    public Observable<AddEmailViewModel> addEmail(RequestParams params) {
        return accountsService.getApi().addEmail(params.getParameters()).map(addEmailMapper);
    }

    public Observable<CheckEmailViewModel> checkEmail(RequestParams params) {
        return accountsService.getApi().checkEmail(params.getParameters())
                .map(checkEmailMapper);
    }

    public Observable<RequestVerificationViewModel> requestVerification(RequestParams params) {
        return accountsService.getApi().requestVerification(params.getParameters())
                .map(requestVerificationMapper);
    }
}
