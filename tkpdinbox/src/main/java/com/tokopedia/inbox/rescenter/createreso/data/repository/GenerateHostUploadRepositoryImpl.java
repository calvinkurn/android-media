package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class GenerateHostUploadRepositoryImpl implements GenerateHostUploadRepository {

    CreateResolutionFactory createResolutionFactory;

    public GenerateHostUploadRepositoryImpl(CreateResolutionFactory createResolutionFactory) {
        this.createResolutionFactory = createResolutionFactory;
    }

    @Override
    public Observable<GenerateHostDomain> generateHost(TKPDMapParam<String, Object> param) {
        return createResolutionFactory.generateHostCloudSource().generateHost(param);
    }

    @Override
    public Observable<UploadDomain> upload(String url,
                                           Map<String, RequestBody> requestBodyMap,
                                           RequestBody generateFile) {
        return createResolutionFactory.getUploadCloudSource().uploadImage(
                url,
                requestBodyMap,
                generateFile);
    }

    @Override
    public Observable<UploadDomain> uploadVideo(String url, Map<String, RequestBody>
            requestBodyMap, MultipartBody.Part videoFile) {
        return createResolutionFactory.getUploadCloudSource().uploadVideo(
                url,
                requestBodyMap,
                videoFile);
    }
}
