package com.tokopedia.session.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.data.model.ValidateOTPDomain;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.otp.phoneverification.data.source.ChangeMsisdnSource;
import com.tokopedia.otp.phoneverification.data.source.VerifyMsisdnSource;
import com.tokopedia.otp.securityquestion.data.source.SecurityQuestionDataSource;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.data.source.CloudDiscoverDataSource;
import com.tokopedia.session.data.source.CreatePasswordDataSource;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.data.source.LocalDiscoverDataSource;
import com.tokopedia.session.data.source.MakeLoginDataSource;
import com.tokopedia.session.data.source.OtpSource;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.register.domain.model.CreatePasswordDomain;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public class SessionRepositoryImpl implements SessionRepository {


    private final CloudDiscoverDataSource cloudDiscoverDataSource;
    private final LocalDiscoverDataSource localDiscoverDataSource;
    private final GetTokenDataSource getTokenDataSource;
    private final CreatePasswordDataSource createPasswordDataSource;
    private final MakeLoginDataSource makeLoginDataSource;
    private final SecurityQuestionDataSource securityQuestionDataSource;
    private final OtpSource otpSource;
    private final ChangeMsisdnSource changeMsisdnSource;
    private final VerifyMsisdnSource verifyMsisdnSource;

    public SessionRepositoryImpl(CloudDiscoverDataSource cloudDiscoverDataSource,
                                 LocalDiscoverDataSource localDiscoverDataSource,
                                 GetTokenDataSource getTokenDataSource,
                                 CreatePasswordDataSource createPasswordDataSource,
                                 MakeLoginDataSource makeLoginDataSource,
                                 SecurityQuestionDataSource securityQuestionDataSource,
                                 OtpSource otpSource,
                                 ChangeMsisdnSource changeMsisdnSource,
                                 VerifyMsisdnSource verifyMsisdnSource) {
        this.cloudDiscoverDataSource = cloudDiscoverDataSource;
        this.localDiscoverDataSource = localDiscoverDataSource;
        this.getTokenDataSource = getTokenDataSource;
        this.createPasswordDataSource = createPasswordDataSource;
        this.makeLoginDataSource = makeLoginDataSource;
        this.securityQuestionDataSource = securityQuestionDataSource;
        this.otpSource = otpSource;
        this.changeMsisdnSource = changeMsisdnSource;
        this.verifyMsisdnSource = verifyMsisdnSource;
    }

    @Override
    public Observable<DiscoverViewModel> getDiscoverFromCloud() {
        return cloudDiscoverDataSource.getDiscover();
    }

    @Override
    public Observable<DiscoverViewModel> getDiscoverFromLocal() {
        return localDiscoverDataSource.getDiscover();
    }

    @Override
    public Observable<TokenViewModel> getAccessToken(RequestParams params) {
        return getTokenDataSource.getAccessToken(params);
    }

    @Override
    public Observable<CreatePasswordDomain> createPassword(RequestParams params) {
        return createPasswordDataSource.createPassword(params);
    }

    @Override
    public Observable<MakeLoginDomain> makeLogin(TKPDMapParam<String, Object> parameters) {
        return makeLoginDataSource.makeLogin(parameters);
    }

    @Override
    public Observable<QuestionDomain> getSecurityQuestionForm(RequestParams params) {
        return securityQuestionDataSource.getSecurityQuestion(params);
    }

    @Override
    public Observable<RequestOtpViewModel> requestOtp(TKPDMapParam<String, Object> parameters) {
        return otpSource.requestOtp(parameters);
    }

    @Override
    public Observable<ValidateOTPDomain> validateOtp(TKPDMapParam<String, Object> parameters) {
        return otpSource.validateOtp(parameters);
    }

    @Override
    public Observable<ChangePhoneNumberViewModel> changePhoneNumber(TKPDMapParam<String, Object> parameters) {
        return changeMsisdnSource.changePhoneNumber(parameters);
    }

    @Override
    public Observable<VerifyPhoneNumberDomain> verifyMsisdn(TKPDMapParam<String, Object> parameters) {
        return verifyMsisdnSource.verifyPhoneNumber(parameters);
    }
}
