package com.tokopedia.otp.data.source;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.mapper.RequestOTPMapper;
import com.tokopedia.otp.domain.mapper.ValidateOTPMapper;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.data.model.ValidateOTPDomain;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/21/17.
 */

public class OtpSource {

    private final ValidateOTPMapper validateOtpMapper;
    private final AccountsService accountsService;
    private final RequestOTPMapper requestOTPMapper;
    private final SessionHandler sessionHandler;


    public OtpSource(AccountsService accountsService, RequestOTPMapper requestOTPMapper,
                     ValidateOTPMapper validateOtpMapper, SessionHandler sessionHandler) {
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

    public Observable<ValidateOTPDomain> validateOtp(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .validateOtp(parameters)
                .map(validateOtpMapper)
                .doOnNext(saveUuid());
    }

    private Action1<ValidateOTPDomain> saveUuid() {
        return new Action1<ValidateOTPDomain>() {
            @Override
            public void call(ValidateOTPDomain validateOtpData) {
                sessionHandler.setUUID(validateOtpData.getUuid());
            }
        };
    }
}
