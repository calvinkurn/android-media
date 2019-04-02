package com.tokopedia.inbox.common.data.source;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.inbox.common.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.common.data.mapper.UploadMapper;
import com.tokopedia.inbox.common.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.common.domain.model.UploadDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionApi;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by yfsx on 30/07/18.
 */
public class ResolutionCommonSource {

    private ResolutionApi resolutionApi;
    private Context context;
    private GenerateHostMapper generateHostMapper;
    private UploadMapper uploadMapper;
    private UploadVideoMapper uploadVideoMapper;

    @Inject
    public ResolutionCommonSource(@ApplicationContext Context context,
                                  ResolutionApi resolutionApi,
                                  GenerateHostMapper generateHostMapper,
                                  UploadMapper uploadMapper,
                                  UploadVideoMapper uploadVideoMapper) {
        this.context = context;
        this.resolutionApi = resolutionApi;
        this.generateHostMapper = generateHostMapper;
        this.uploadMapper = uploadMapper;
        this.uploadVideoMapper = uploadVideoMapper;
    }

    public Observable<GenerateHostDomain> generateHost(RequestParams requestParams) {
        return resolutionApi.generateHost(requestParams.getParameters())
                .map(generateHostMapper);
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
