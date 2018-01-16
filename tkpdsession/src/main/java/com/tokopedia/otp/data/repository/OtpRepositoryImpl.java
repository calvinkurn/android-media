package com.tokopedia.otp.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.data.model.RequestOtpModel;
import com.tokopedia.otp.data.model.ValidateOtpModel;
import com.tokopedia.otp.data.factory.OtpSourceFactory;
import com.tokopedia.otp.domainold.OtpRepository;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 * @deprecated  do not use. Use UseCase instead.
 */

@Deprecated
public class OtpRepositoryImpl implements OtpRepository {

    private final OtpSourceFactory otpSourceFactory;

    public OtpRepositoryImpl(OtpSourceFactory otpSourceFactory) {
        this.otpSourceFactory = otpSourceFactory;
    }

    @Override
    public Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> parameters) {
        return otpSourceFactory.createCloudOtpSource().requestOtp(parameters);
    }

    @Override
    public Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> parameters) {
        return otpSourceFactory.createCloudOtpSource().validateOtp(parameters);
    }
}
