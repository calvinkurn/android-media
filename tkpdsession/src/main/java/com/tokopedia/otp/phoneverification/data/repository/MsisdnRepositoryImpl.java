package com.tokopedia.otp.phoneverification.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
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
    public Observable<VerifyPhoneNumberDomain> verifyMsisdn(TKPDMapParam<String, Object> parameters) {
        return msisdnSourceFactory.createCloudVerifyMsisdnSource().verifyPhoneNumber(parameters);
    }
}

