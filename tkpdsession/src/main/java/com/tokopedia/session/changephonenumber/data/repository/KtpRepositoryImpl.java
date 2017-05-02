package com.tokopedia.session.changephonenumber.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.CheckStatusModel;
import com.tokopedia.session.changephonenumber.data.factory.KtpSourceFactory;
import com.tokopedia.session.changephonenumber.data.factory.UploadImageSourceFactory;
import com.tokopedia.session.changephonenumber.domain.KtpRepository;

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
