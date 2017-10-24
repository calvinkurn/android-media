package com.tokopedia.session.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.data.model.ValidateOTPDomain;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.domain.model.CreatePasswordDomain;

import rx.Observable;

/**
 * @author by nisie on 10/10/17.
 */

public interface SessionRepository {


    Observable<DiscoverViewModel> getDiscoverFromCloud();

    Observable<DiscoverViewModel> getDiscoverFromLocal();

    Observable<TokenViewModel> getAccessToken(RequestParams params);

    Observable<CreatePasswordDomain> createPassword(RequestParams requestParams);

    Observable<MakeLoginDomain> makeLogin(TKPDMapParam<String, Object> parameters);

    Observable<QuestionDomain> getSecurityQuestionForm(RequestParams requestParams);

    Observable<RequestOtpViewModel> requestOtp(TKPDMapParam<String, Object> parameters);

    Observable<ValidateOTPDomain> validateOtp(TKPDMapParam<String, Object> parameters);

    Observable<ChangePhoneNumberViewModel> changePhoneNumber(TKPDMapParam<String, Object> parameters);

    Observable<VerifyPhoneNumberDomain> verifyMsisdn(TKPDMapParam<String, Object> parameters);
}
