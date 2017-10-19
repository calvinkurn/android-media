package com.tokopedia.session.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

import rx.Observable;

/**
 * @author by nisie on 10/18/17.
 */

public class MakeLoginDataSource {

    private final AccountsService accountsService;
    private final MakeLoginMapper makeLoginMapper;

    public MakeLoginDataSource(AccountsService accountsService, MakeLoginMapper makeLoginMapper) {
        this.accountsService = accountsService;
        this.makeLoginMapper = makeLoginMapper;
    }

    public Observable<MakeLoginDomain> makeLogin(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .makeLogin(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(), parameters))
                .map(makeLoginMapper);
    }
}
