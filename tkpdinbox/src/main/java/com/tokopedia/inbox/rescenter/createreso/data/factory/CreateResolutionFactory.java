package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateSubmitCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateValidateCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GenerateHostCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.UploadCloudSource;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResolutionFactory {
    private final UploadVideoMapper uploadVideoMapper;
    private Context context;
    private CreateValidateMapper createValidateMapper;
    private GenerateHostMapper generateHostMapper;
    private UploadMapper uploadMapper;
    private CreateSubmitMapper createSubmitMapper;
    private ResolutionApi resolutionApi;
    private ResCenterActService resCenterActService;

    public CreateResolutionFactory(Context context,
                                   CreateValidateMapper createValidateMapper,
                                   GenerateHostMapper generateHostMapper,
                                   UploadMapper uploadMapper,
                                   CreateSubmitMapper createSubmitMapper,
                                   ResolutionApi resolutionApi,
                                   ResCenterActService resCenterActService,
                                   UploadVideoMapper uploadVideoMapper
                                   ) {
        this.context = context;
        this.createValidateMapper = createValidateMapper;
        this.generateHostMapper = generateHostMapper;
        this.uploadMapper = uploadMapper;
        this.createSubmitMapper = createSubmitMapper;
        this.resolutionApi = resolutionApi;
        this.resCenterActService = resCenterActService;
        this.uploadVideoMapper = uploadVideoMapper;
    }

    public CreateValidateCloudSource getCreateValidateCloudSource() {
        return new CreateValidateCloudSource(createValidateMapper, resolutionApi);
    }

    public GenerateHostCloudSource generateHostCloudSource() {
        return new GenerateHostCloudSource(context, generateHostMapper, resCenterActService);
    }

    public UploadCloudSource getUploadCloudSource() {
        return new UploadCloudSource(uploadMapper, resolutionApi, uploadVideoMapper);
    }

    public CreateSubmitCloudSource createSubmitCloudSource() {
        return new CreateSubmitCloudSource(createSubmitMapper, resolutionApi);
    }
}