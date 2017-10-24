package com.tokopedia.otp.securityquestion.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.CheckStatusModel;
import com.tokopedia.otp.securityquestion.data.factory.KtpSourceFactory;

import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public class KtpRepositoryImpl implements KtpRepository {
    private final KtpSourceFactory ktpSourceFactory;

    public KtpRepositoryImpl(KtpSourceFactory ktpSourceFactory) {

        this.ktpSourceFactory = ktpSourceFactory;
    }

    @Override
    public Observable<CheckStatusModel> checkStatus(TKPDMapParam<String, Object> parameters) {
        return ktpSourceFactory.createCloudUploadHostDataStore().checkStatus(parameters);
    }
}
