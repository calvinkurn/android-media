package com.tokopedia.otp.tokocashotp.data.source;

import com.tokopedia.core.network.apiservices.tokocash.WalletBaseService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.tokocashotp.data.mapper.GetAccessTokenTokoCashMapper;
import com.tokopedia.otp.tokocashotp.domain.model.AccessTokenTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/5/17.
 */

public class TokoCashTokenSource {

    private final WalletBaseService walletService;
    private final GetAccessTokenTokoCashMapper getAccessTokenTokoCashMapper;

    @Inject
    public TokoCashTokenSource(WalletBaseService walletService,
                               GetAccessTokenTokoCashMapper getAccessTokenTokoCashMapper) {
        this.walletService = walletService;
        this.getAccessTokenTokoCashMapper = getAccessTokenTokoCashMapper;
    }

    public Observable<AccessTokenTokoCashDomain> getAccessToken(TKPDMapParam<String, Object> parameters) {
        return walletService.getApi().getAccessToken(parameters)
                .map(getAccessTokenTokoCashMapper);
    }
}
