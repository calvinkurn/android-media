package com.tokopedia.otp.phoneverification.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public interface MsisdnRepository {

    Observable<VerifyPhoneNumberModel> verifyMsisdn(TKPDMapParam<String, Object> parameters);

}
