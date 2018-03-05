package com.tokopedia.otp.registerphonenumber.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.otp.registerphonenumber.data.mapper.RequestOtpMapper;
import com.tokopedia.otp.registerphonenumber.data.mapper.VerifyOtpMapper;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.VerifyOtpViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 5/3/18.
 */

public class RegisterPhoneNumberOtpSource {

    private final AccountsService accountsService;
    private final RequestOtpMapper requestOtpMapper;
    private final VerifyOtpMapper verifyOtpMapper;

    @Inject
    public RegisterPhoneNumberOtpSource(AccountsService accountsService,
                                        RequestOtpMapper requestOtpMapper,
                                        VerifyOtpMapper verifyOtpMapper) {
        this.accountsService = accountsService;
        this.requestOtpMapper = requestOtpMapper;
        this.verifyOtpMapper = verifyOtpMapper;
    }

    public Observable<RequestOtpViewModel> requestRegisterOtp(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi().requestRegisterOtp(parameters)
                .map(requestOtpMapper);
    }

    public Observable<VerifyOtpViewModel> verifyRegisterOtp(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi().verifyRegisterOtp(parameters)
                .map(verifyOtpMapper);
    }
}
