package com.tokopedia.session.login.loginphonenumber.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.tokocash.WalletBasicService;
import com.tokopedia.session.login.loginphonenumber.data.mapper.WalletTokenMapper;
import com.tokopedia.session.login.loginphonenumber.domain.model.AccessTokenTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/6/17.
 */

public class WalletTokenSource {

    WalletBasicService walletService;
    WalletTokenMapper walletTokenMapper;

    @Inject
    public WalletTokenSource(WalletBasicService walletService, WalletTokenMapper walletTokenMapper) {
        this.walletService = walletService;
        this.walletTokenMapper = walletTokenMapper;
    }

    public Observable<AccessTokenTokoCashDomain> getAccessToken(RequestParams requestParams) {
        return walletService.getApi().getToken(requestParams.getParameters())
                .map(walletTokenMapper);
    }
}
