package com.tokopedia.otp.data.source;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.mapper.RequestOtpMapper;
import com.tokopedia.otp.domain.mapper.ValidateOtpMapper;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.data.model.ValidateOtpDomain;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/21/17.
 */

public class OtpSource {

    private final ValidateOtpMapper validateOtpMapper;
    private final AccountsService accountsService;
    private final RequestOtpMapper requestOTPMapper;
    private final SessionHandler sessionHandler;

    public OtpSource(AccountsService accountsService, RequestOtpMapper requestOTPMapper,
                     ValidateOtpMapper validateOtpMapper, SessionHandler sessionHandler) {
        this.accountsService = accountsService;
        this.requestOTPMapper = requestOTPMapper;
        this.validateOtpMapper = validateOtpMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<RequestOtpViewModel> requestOtp(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .requestOtp((String) parameters.get(RequestOtpUseCase.PARAM_USER_ID),
                        parameters)
                .map(requestOTPMapper);
    }

    public Observable<ValidateOtpDomain> validateOtp(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .validateOtp(parameters)
                .map(validateOtpMapper)
                .doOnNext(saveUuid());
    }

    private Action1<ValidateOtpDomain> saveUuid() {
        return new Action1<ValidateOtpDomain>() {
            @Override
            public void call(ValidateOtpDomain validateOtpData) {
                sessionHandler.setUUID(validateOtpData.getUuid());
            }
        };
    }

    public Observable<RequestOtpViewModel> requestOtpWithEmail(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .requestOtpToEmail((String) parameters.get(RequestOtpUseCase.PARAM_USER_ID),
                        parameters)
                .map(requestOTPMapper);
    }
}
