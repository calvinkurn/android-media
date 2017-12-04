package com.tokopedia.inbox.rescenter.createreso.data.source.cloud;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class UploadCloudSource {
    private final UploadVideoMapper uploadVideoMapper;
    private UploadMapper uploadMapper;
    private ResolutionApi resolutionApi;

    public UploadCloudSource(UploadMapper uploadMapper,
                             ResolutionApi resolutionApi,
                             UploadVideoMapper uploadVideoMapper) {
        this.uploadMapper = uploadMapper;
        this.resolutionApi = resolutionApi;
        this.uploadVideoMapper = uploadVideoMapper;
    }

    public Observable<UploadDomain> uploadImage(String url,
                                                Map<String, RequestBody> params,
                                                RequestBody imageFile) {
        return resolutionApi.uploadImage(url, params, imageFile)
                .map(uploadMapper);
    }

    public Observable<UploadDomain> uploadVideo(String url, Map<String, RequestBody> params,
                                                MultipartBody.Part videoFile) {
        return resolutionApi.uploadVideo(url, params, videoFile)
                .map(uploadVideoMapper);
    }
}
