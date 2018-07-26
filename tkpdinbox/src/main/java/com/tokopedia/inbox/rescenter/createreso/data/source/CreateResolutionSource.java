package com.tokopedia.inbox.rescenter.createreso.data.source;

import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoWithoutAttachmentMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoWithoutAttachmentDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoWithoutAttachmentUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;
import com.tokopedia.inbox.rescenter.network.ResolutionApi;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResolutionSource {

//    private final UploadVideoMapper uploadVideoMapper;
    private GetProductProblemMapper productProblemMapper;
//    private SolutionMapper solutionMapper;
    private CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper;
//    private CreateValidateMapper createValidateMapper;
//    private GenerateHostMapper generateHostMapper;
//    private UploadMapper uploadMapper;
//    private CreateSubmitMapper createSubmitMapper;
    private ResolutionApi resolutionApi;
//    private ResCenterActService resCenterActService;
//    private EditSolutionMapper editSolutionMapper;
//    private AppealSolutionMapper appealSolutionMapper;
//    private EditAppealResolutionResponseMapper editAppealResolutionResponseMapper;

//    @Inject
//    public CreateResolutionSource(GetProductProblemMapper productProblemMapper,
//                                  ResolutionApi resolutionApi) {
//        this.productProblemMapper = productProblemMapper;
//        this.resolutionApi = resolutionApi;
//    }
    @Inject
    public CreateResolutionSource(GetProductProblemMapper productProblemMapper,
//                                  SolutionMapper solutionMapper,
                                  CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper,
//                                  CreateValidateMapper createValidateMapper,
//                                  GenerateHostMapper generateHostMapper,
//                                  UploadMapper uploadMapper,
//                                  CreateSubmitMapper createSubmitMapper,
                                  ResolutionApi resolutionApi
//                                  ResCenterActService resCenterActService,
//                                  UploadVideoMapper uploadVideoMapper,
//                                  EditSolutionMapper editSolutionMapper,
//                                  AppealSolutionMapper appealSolutionMapper,
//                                  EditAppealResolutionResponseMapper editAppealResolutionResponseMapper
    ){
        this.productProblemMapper = productProblemMapper;
//        this.solutionMapper = solutionMapper;
        this.createResoWithoutAttachmentMapper = createResoWithoutAttachmentMapper;
//        this.createValidateMapper = createValidateMapper;
//        this.generateHostMapper = generateHostMapper;
//        this.uploadMapper = uploadMapper;
//        this.createSubmitMapper = createSubmitMapper;
        this.resolutionApi = resolutionApi;
//        this.resCenterActService = resCenterActService;
//        this.uploadVideoMapper = uploadVideoMapper;
//        this.editSolutionMapper = editSolutionMapper;
//        this.appealSolutionMapper = appealSolutionMapper;
//        this.editAppealResolutionResponseMapper = editAppealResolutionResponseMapper;
    }

    public Observable<ProductProblemResponseDomain> getProductProblemList(RequestParams requestParams) {
        return resolutionApi.getProductProblemList(
                requestParams.getString(GetProductProblemUseCase.ORDER_ID, ""),
                requestParams.getParameters())
                .map(productProblemMapper);
    }

    public Observable<CreateResoWithoutAttachmentDomain> createResoWithoutAttachmentResponse(RequestParams requestParams) {
        try {
            return resolutionApi.postCreateResolution(requestParams.getString(
                    CreateResoWithoutAttachmentUseCase.ORDER_ID, ""),
                    requestParams.getString(CreateResoWithoutAttachmentUseCase.PARAM_RESULT, ""))
                    .map(createResoWithoutAttachmentMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
