package com.tokopedia.session.data.source;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.entity.phoneverification.ValidateOtpData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.mapper.RequestOTPMapper;
import com.tokopedia.otp.data.mapper.ValidateOTPMapper;
import com.tokopedia.otp.data.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.model.ValidateOTPDomain;

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
