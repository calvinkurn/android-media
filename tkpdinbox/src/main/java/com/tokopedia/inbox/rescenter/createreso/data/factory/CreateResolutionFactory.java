package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.apiservices.upload.apis.UploadImageActApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep1Mapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep2Mapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditAppealResolutionResponseMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateResoStep1CloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateResoStep2CloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateSubmitCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateValidateCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GenerateHostCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetEditSolutionCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetProductProblemCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetSolutionCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.PostEditSolutionCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.UploadCloudSource;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResolutionFactory {
    private final UploadVideoMapper uploadVideoMapper;
    private Context context;
    private GetProductProblemMapper productProblemMapper;
    private SolutionMapper solutionMapper;
    private CreateResoStep1Mapper createResoStep1Mapper;
    private CreateResoStep2Mapper createResoStep2Mapper;
    private CreateValidateMapper createValidateMapper;
    private GenerateHostMapper generateHostMapper;
    private UploadMapper uploadMapper;
    private CreateSubmitMapper createSubmitMapper;
    private ResolutionApi resolutionApi;
    private ResCenterActService resCenterActService;
    private EditSolutionMapper editSolutionMapper;
    private EditAppealResolutionResponseMapper editAppealResolutionResponseMapper;

    public CreateResolutionFactory(Context context,
                                   GetProductProblemMapper productProblemMapper,
                                   SolutionMapper solutionMapper,
                                   CreateResoStep1Mapper createResoStep1Mapper,
                                   CreateResoStep2Mapper createResoStep2Mapper,
                                   CreateValidateMapper createValidateMapper,
                                   GenerateHostMapper generateHostMapper,
                                   UploadMapper uploadMapper,
                                   CreateSubmitMapper createSubmitMapper,
                                   ResolutionApi resolutionApi,
                                   ResCenterActService resCenterActService,
                                   UploadVideoMapper uploadVideoMapper,
                                   EditSolutionMapper editSolutionMapper,
                                   EditAppealResolutionResponseMapper editAppealResolutionResponseMapper) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.solutionMapper = solutionMapper;
        this.createResoStep1Mapper = createResoStep1Mapper;
        this.createResoStep2Mapper = createResoStep2Mapper;
        this.createValidateMapper = createValidateMapper;
        this.generateHostMapper = generateHostMapper;
        this.uploadMapper = uploadMapper;
        this.createSubmitMapper = createSubmitMapper;
        this.resolutionApi = resolutionApi;
        this.resCenterActService = resCenterActService;
        this.uploadVideoMapper = uploadVideoMapper;
        this.editSolutionMapper = editSolutionMapper;
        this.editAppealResolutionResponseMapper = editAppealResolutionResponseMapper;
    }

    public GetProductProblemCloudSource getProductProblemCloudSource() {
        return new GetProductProblemCloudSource(context, productProblemMapper, resolutionApi);
    }

    public GetSolutionCloudSource getSolutionCloudSource() {
        return new GetSolutionCloudSource(context, solutionMapper, resolutionApi);
    }

    public CreateResoStep1CloudSource createResoStep1CloudSource() {
        return new CreateResoStep1CloudSource(context, createResoStep1Mapper, resolutionApi);
    }

    public CreateResoStep2CloudSource createResoStep2CloudSource() {
        return new CreateResoStep2CloudSource(context, createResoStep2Mapper, resolutionApi);
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

    public GetEditSolutionCloudSource createEditSolutionCloudSource() {
        return new GetEditSolutionCloudSource(editSolutionMapper, resolutionApi);
    }

    public PostEditSolutionCloudSource postEditSolutionDataSource() {
        return new PostEditSolutionCloudSource(editAppealResolutionResponseMapper, resolutionApi);
    }
}