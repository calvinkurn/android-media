package com.tokopedia.session.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.entity.otp.RequestOtpData;
import com.tokopedia.core.network.entity.phoneverification.ValidateOtpData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.data.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.domain.model.ValidateOTPDomain;
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
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

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

    public SessionRepositoryImpl(CloudDiscoverDataSource cloudDiscoverDataSource,
                                 LocalDiscoverDataSource localDiscoverDataSource,
                                 GetTokenDataSource getTokenDataSource,
                                 CreatePasswordDataSource createPasswordDataSource,
                                 MakeLoginDataSource makeLoginDataSource,
                                 SecurityQuestionDataSource securityQuestionDataSource,
                                 OtpSource otpSource) {
        this.cloudDiscoverDataSource = cloudDiscoverDataSource;
        this.localDiscoverDataSource = localDiscoverDataSource;
        this.getTokenDataSource = getTokenDataSource;
        this.createPasswordDataSource = createPasswordDataSource;
        this.makeLoginDataSource = makeLoginDataSource;
        this.securityQuestionDataSource = securityQuestionDataSource;
        this.otpSource = otpSource;
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
    public Observable<CreatePasswordViewModel> createPassword(RequestParams params) {
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
}
