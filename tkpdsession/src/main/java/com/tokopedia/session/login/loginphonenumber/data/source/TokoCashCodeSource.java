package com.tokopedia.session.login.loginphonenumber.data.source;

import com.tokopedia.core.network.apiservices.tokocash.WalletBaseService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.login.loginphonenumber.data.mapper.GetCodeTokoCashMapper;
import com.tokopedia.session.login.loginphonenumber.domain.model.CodeTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/5/17.
 */

public class TokoCashCodeSource {

    private final WalletBaseService walletService;
    private final GetCodeTokoCashMapper getCodeTokoCashMapper;

    @Inject
    public TokoCashCodeSource(WalletBaseService walletService,
                              GetCodeTokoCashMapper getCodeTokoCashMapper) {
        this.walletService = walletService;
        this.getCodeTokoCashMapper = getCodeTokoCashMapper;
    }

    public Observable<CodeTokoCashDomain> getAccessToken(TKPDMapParam<String, Object> parameters) {
        return walletService.getApi().getAuthorizeCode(parameters)
                .map(getCodeTokoCashMapper);
    }
}
