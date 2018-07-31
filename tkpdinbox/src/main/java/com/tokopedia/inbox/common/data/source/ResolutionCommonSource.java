package com.tokopedia.inbox.common.data.source;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.inbox.common.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.common.data.mapper.UploadMapper;
import com.tokopedia.inbox.common.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.common.domain.model.UploadDomain;
import com.tokopedia.inbox.rescenter.di.ResolutionModule;
import com.tokopedia.inbox.rescenter.network.ResolutionApi;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * @author by yfsx on 30/07/18.
 */
public class ResolutionCommonSource {

    private ResolutionApi resolutionApi;
    private ResolutionApi resolutionImageServiceApi;
    private Context context;
    private UserSession userSession;
    private GenerateHostMapper generateHostMapper;
    private UploadMapper uploadMapper;
    private UploadVideoMapper uploadVideoMapper;

    @Inject
    public ResolutionCommonSource(@ApplicationContext Context context,
                                  @Named(ResolutionModule.RESOLUTION_SERVICE) ResolutionApi resolutionApi,
                                  @Named(ResolutionModule.RESOLUTION_IMAGE_SERVICE) ResolutionApi resolutionImageServiceApi,
                                  UserSession userSession,
                                  GenerateHostMapper generateHostMapper,
                                  UploadMapper uploadMapper,
                                  UploadVideoMapper uploadVideoMapper) {
        this.context = context;
        this.userSession = userSession;
        this.resolutionApi = resolutionApi;
        this.resolutionImageServiceApi = resolutionImageServiceApi;
        this.generateHostMapper = generateHostMapper;
        this.uploadMapper = uploadMapper;
        this.uploadVideoMapper = uploadVideoMapper;
    }

    public Observable<GenerateHostDomain> generateHost(TKPDMapParam<String, Object> params) {
        return resolutionApi.generateTokenHostWithoutHeader(
                AuthUtil.generateParamsNetwork2(context, params, userSession.getDeviceId(), userSession.getUserId()))
                .map(generateHostMapper);
    }

    public Observable<UploadDomain> uploadImage(String url,
                                                Map<String, RequestBody> params,
                                                RequestBody imageFile) {
        return resolutionImageServiceApi.uploadImage(url, params, imageFile)
                .map(uploadMapper);
    }

    public Observable<UploadDomain> uploadVideo(String url, Map<String, RequestBody> params,
                                                MultipartBody.Part videoFile) {
        return resolutionImageServiceApi.uploadVideo(url, params, videoFile)
                .map(uploadVideoMapper);
    }

}
