package com.tokopedia.otp.phoneverification.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;
import com.tokopedia.otp.phoneverification.data.factory.MsisdnSourceFactory;
import com.tokopedia.otp.phoneverification.domain.MsisdnRepository;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class MsisdnRepositoryImpl implements MsisdnRepository {
    private final MsisdnSourceFactory msisdnSourceFactory;

    public MsisdnRepositoryImpl(MsisdnSourceFactory msisdnSourceFactory) {
        this.msisdnSourceFactory = msisdnSourceFactory;
    }

    @Override
    public Observable<VerifyPhoneNumberModel> verifyMsisdn(TKPDMapParam<String, Object> parameters) {
        return msisdnSourceFactory.createCloudOtpSource().verifyPhoneNumberModel(parameters);
    }
}

