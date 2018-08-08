package com.tokopedia.session.login.loginphonenumber.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.tokocash.WalletBaseService;
import com.tokopedia.session.login.loginphonenumber.data.mapper.CheckMsisdnTokoCashMapper;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.CheckMsisdnTokoCashViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashSource {

    WalletBaseService walletBaseService;
    CheckMsisdnTokoCashMapper checkMsisdnTokoCashMapper;

    @Inject
    public CheckMsisdnTokoCashSource(WalletBaseService walletBaseService,
                                     CheckMsisdnTokoCashMapper checkMsisdnTokoCashMapper) {
        this.walletBaseService = walletBaseService;
        this.checkMsisdnTokoCashMapper = checkMsisdnTokoCashMapper;
    }

    public Observable<CheckMsisdnTokoCashViewModel> checkMsisdn(RequestParams requestParams) {
        return walletBaseService.getApi().checkMsisdn(requestParams.getParameters())
                .map(checkMsisdnTokoCashMapper);
    }
}
