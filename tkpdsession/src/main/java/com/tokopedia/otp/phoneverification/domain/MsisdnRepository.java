package com.tokopedia.otp.phoneverification.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public interface MsisdnRepository {

    Observable<VerifyPhoneNumberDomain> verifyMsisdn(TKPDMapParam<String, Object> parameters);

}
