package com.tokopedia.otp.domainold;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.data.model.RequestOtpModel;
import com.tokopedia.otp.data.model.ValidateOtpModel;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 * @deprecated do not use.
 */
@Deprecated
public interface OtpRepository {

    Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> parameters);

    Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> parameters);

}
