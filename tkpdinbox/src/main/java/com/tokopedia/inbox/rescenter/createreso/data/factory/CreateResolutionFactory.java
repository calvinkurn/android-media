package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.AppealSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoWithoutAttachmentMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditAppealResolutionResponseMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateResoWithoutAttachmentCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateSubmitCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.CreateValidateCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GenerateHostCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetAppealSolutionCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetEditSolutionCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetProductProblemCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.GetSolutionCloudSource;
import com.tokopedia.inbox.rescenter.createreso.data.source.cloud.PostAppealSolutionCloudSource;
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
    private CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper;
    private CreateValidateMapper createValidateMapper;
    private GenerateHostMapper generateHostMapper;
    private UploadMapper uploadMapper;
    private CreateSubmitMapper createSubmitMapper;
    private ResolutionApi resolutionApi;
    private ResCenterActService resCenterActService;
    private EditSolutionMapper editSolutionMapper;
    private AppealSolutionMapper appealSolutionMapper;
    private EditAppealResolutionResponseMapper editAppealResolutionResponseMapper;

    public CreateResolutionFactory(Context context,
                                   GetProductProblemMapper productProblemMapper,
                                   SolutionMapper solutionMapper,
                                   CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper,
                                   CreateValidateMapper createValidateMapper,
                                   GenerateHostMapper generateHostMapper,
                                   UploadMapper uploadMapper,
                                   CreateSubmitMapper createSubmitMapper,
                                   ResolutionApi resolutionApi,
                                   ResCenterActService resCenterActService,
                                   UploadVideoMapper uploadVideoMapper,
                                   EditSolutionMapper editSolutionMapper,
                                   AppealSolutionMapper appealSolutionMapper,
                                   EditAppealResolutionResponseMapper editAppealResolutionResponseMapper) {
        this.context = context;
        this.productProblemMapper = productProblemMapper;
        this.solutionMapper = solutionMapper;
        this.createResoWithoutAttachmentMapper = createResoWithoutAttachmentMapper;
        this.createValidateMapper = createValidateMapper;
        this.generateHostMapper = generateHostMapper;
        this.uploadMapper = uploadMapper;
        this.createSubmitMapper = createSubmitMapper;
        this.resolutionApi = resolutionApi;
        this.resCenterActService = resCenterActService;
        this.uploadVideoMapper = uploadVideoMapper;
        this.editSolutionMapper = editSolutionMapper;
        this.appealSolutionMapper = appealSolutionMapper;
        this.editAppealResolutionResponseMapper = editAppealResolutionResponseMapper;
    }

    public GetProductProblemCloudSource getProductProblemCloudSource() {
        return new GetProductProblemCloudSource(context, productProblemMapper, resolutionApi);
    }

    public GetSolutionCloudSource getSolutionCloudSource() {
        return new GetSolutionCloudSource(context, solutionMapper, resolutionApi);
    }

    public CreateResoWithoutAttachmentCloudSource createResoStep1CloudSource() {
        return new CreateResoWithoutAttachmentCloudSource(context, createResoWithoutAttachmentMapper, resolutionApi);
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

    public GetAppealSolutionCloudSource createAppealSolutionCloudSource() {
        return new GetAppealSolutionCloudSource(appealSolutionMapper, resolutionApi);
    }

    public PostEditSolutionCloudSource postEditSolutionDataSource() {
        return new PostEditSolutionCloudSource(editAppealResolutionResponseMapper, resolutionApi);
    }

    public PostAppealSolutionCloudSource postAppealSolutionCloudSource() {
        return new PostAppealSolutionCloudSource(editAppealResolutionResponseMapper, resolutionApi);
    }
}