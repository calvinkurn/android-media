package com.tokopedia.session.register.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.domain.model.TokenViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/11/17.
 */

public class CloudTokenDataSource {
    private final AccountsService accountsService;
    private final TokenMapper tokenMapper;

    public CloudTokenDataSource(AccountsService accountsService, TokenMapper tokenMapper) {
        this.accountsService = accountsService;
        this.tokenMapper = tokenMapper;
    }

    public Observable<TokenViewModel> getAccessToken(RequestParams params) {
        return accountsService.getApi()
                .getToken(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                        params.getParameters()))
                .map(tokenMapper);

    }
}
