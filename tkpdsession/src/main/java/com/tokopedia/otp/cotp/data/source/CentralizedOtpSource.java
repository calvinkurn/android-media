package com.tokopedia.otp.cotp.data.source;

import com.tokopedia.core.network.apiservices.tokocash.WalletBaseService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.cotp.data.mapper.RequestOtpMapper;
import com.tokopedia.otp.cotp.data.mapper.VerifyOtpMapper;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerifyOtpViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */

public class CentralizedOtpSource {

    private final WalletBaseService walletService;
    private final RequestOtpMapper requestOtpTokoCashMapper;
    private final VerifyOtpMapper verifyOtpTokoCashMapper;

    @Inject
    public CentralizedOtpSource(WalletBaseService walletService,
                                RequestOtpMapper requestOtpTokoCashMapper,
                                VerifyOtpMapper verifyOtpTokoCashMapper) {
        this.walletService = walletService;
        this.requestOtpTokoCashMapper = requestOtpTokoCashMapper;
        this.verifyOtpTokoCashMapper = verifyOtpTokoCashMapper;
    }

    public Observable<RequestOtpViewModel> requestLoginOtp(TKPDMapParam<String, Object> parameters) {
        return walletService.getApi().requestLoginOtp(parameters)
                .map(requestOtpTokoCashMapper);
    }

    public Observable<VerifyOtpViewModel> verifyOtpTokoCash(TKPDMapParam<String, Object> parameters) {
        return walletService.getApi().verifyLoginOtp(parameters)
                .map(verifyOtpTokoCashMapper);
    }
}
