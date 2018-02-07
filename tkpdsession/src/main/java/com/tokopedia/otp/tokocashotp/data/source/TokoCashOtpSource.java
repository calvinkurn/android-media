package com.tokopedia.otp.tokocashotp.data.source;

import com.tokopedia.core.network.apiservices.tokocash.WalletBaseService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.tokocashotp.data.mapper.RequestOtpTokoCashMapper;
import com.tokopedia.otp.tokocashotp.data.mapper.VerifyOtpTokoCashMapper;
import com.tokopedia.otp.tokocashotp.view.viewmodel.RequestOtpTokoCashViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */

public class TokoCashOtpSource {

    private final WalletBaseService walletService;
    private final RequestOtpTokoCashMapper requestOtpTokoCashMapper;
    private final VerifyOtpTokoCashMapper verifyOtpTokoCashMapper;

    @Inject
    public TokoCashOtpSource(WalletBaseService walletService,
                             RequestOtpTokoCashMapper requestOtpTokoCashMapper,
                             VerifyOtpTokoCashMapper verifyOtpTokoCashMapper) {
        this.walletService = walletService;
        this.requestOtpTokoCashMapper = requestOtpTokoCashMapper;
        this.verifyOtpTokoCashMapper = verifyOtpTokoCashMapper;
    }

    public Observable<RequestOtpTokoCashViewModel> requestLoginOtp(TKPDMapParam<String, Object> parameters) {
        return walletService.getApi().requestLoginOtp(parameters)
                .map(requestOtpTokoCashMapper);
    }

    public Observable<VerifyOtpTokoCashViewModel> verifyOtpTokoCash(TKPDMapParam<String, Object> parameters) {
        return walletService.getApi().verifyLoginOtp(parameters)
                .map(verifyOtpTokoCashMapper);
    }
}
