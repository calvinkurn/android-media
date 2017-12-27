package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoRequestDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateSubmitUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateValidateUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.GenerateHostUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.UploadUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.UploadVideoUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResoWithAttachmentUseCase extends UseCase<CreateSubmitDomain> {

    public static final String ORDER_ID = "order_id";
    private static final String PARAM_LIST_ATTACHMENT = "LIST_ATTACHMENT";
    public static final String RESOLUTION_ID = "resolutionID";
    private final UploadVideoUseCase uploadVideoUseCase;

    private CreateValidateUseCase createValidateUseCase;
    private GenerateHostUseCase generateHostUseCase;
    private UploadUseCase uploadUseCase;
    private CreateSubmitUseCase createSubmitUseCase;

    public CreateResoWithAttachmentUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           CreateValidateUseCase createValidateUseCase,
                                           GenerateHostUseCase generateHostUseCase,
                                           UploadUseCase uploadUseCase,
                                           CreateSubmitUseCase createSubmitUseCase,
                                           UploadVideoUseCase uploadVideoUseCase) {
        super(threadExecutor, postExecutionThread);
        this.createValidateUseCase = createValidateUseCase;
        this.generateHostUseCase = generateHostUseCase;
        this.uploadUseCase = uploadUseCase;
        this.createSubmitUseCase = createSubmitUseCase;
        this.uploadVideoUseCase = uploadVideoUseCase;
    }

    @Override
    public Observable<CreateSubmitDomain> createObservable(RequestParams requestParams) {
        final CreateResoRequestDomain createResoRequestDomain = new CreateResoRequestDomain();
        createResoRequestDomain.setOrderId(requestParams.getString(ORDER_ID, ""));
        createResoRequestDomain.setResolutionId(requestParams.getString(RESOLUTION_ID, ""));
        return getObservableValidateCreateReso(requestParams, createResoRequestDomain)
                .flatMap(getObservableGenerateHost(requestParams))
                .flatMap(addGenerateHostResultToRequestModel(createResoRequestDomain))
                .flatMap(getObservableUploadAttachment(getListAttachment(requestParams)))
                .flatMap(addListAttachmentUploadToRequestModel(createResoRequestDomain))
                .flatMap(getObservableCreateSubmitReso(createResoRequestDomain))
                .flatMap(addCreateSubmitToRequestModel(createResoRequestDomain))
                .flatMap(mappingResultToDomain());
    }


    private Observable<CreateResoRequestDomain> getObservableValidateCreateReso(
            RequestParams requestParams, final CreateResoRequestDomain createResoRequestDomain) {
        return createValidateUseCase.createObservable(requestParams)
                .flatMap(addValidateResultToRequestModel(createResoRequestDomain));
    }

    private Func1<CreateValidateDomain, Observable<CreateResoRequestDomain>>
    addValidateResultToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return new Func1<CreateValidateDomain, Observable<CreateResoRequestDomain>>() {
            @Override
            public Observable<CreateResoRequestDomain> call(
                    CreateValidateDomain createValidateDomain) {
                createResoRequestDomain.setCreateValidateDomain(createValidateDomain);
                return Observable.just(createResoRequestDomain);
            }
        };
    }

    private Func1<CreateResoRequestDomain, Observable<GenerateHostDomain>>
    getObservableGenerateHost(final RequestParams param) {
        return new Func1<CreateResoRequestDomain, Observable<GenerateHostDomain>>() {
            @Override
            public Observable<GenerateHostDomain> call(CreateResoRequestDomain createResoRequestDomain) {
                return generateHostUseCase.createObservable(param);
            }
        };
    }

    private Func1<GenerateHostDomain, Observable<CreateResoRequestDomain>>
    addGenerateHostResultToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return new Func1<GenerateHostDomain, Observable<CreateResoRequestDomain>>() {
            @Override
            public Observable<CreateResoRequestDomain> call(GenerateHostDomain generateHostDomain) {
                createResoRequestDomain.setGenerateHostDomain(generateHostDomain);
                return Observable.just(createResoRequestDomain);
            }
        };
    }

    private List<AttachmentViewModel> getListAttachment(RequestParams requestParams) {
        return (List<AttachmentViewModel>) requestParams.getObject(PARAM_LIST_ATTACHMENT);
    }

    private Func1<CreateResoRequestDomain, Observable<List<UploadDomain>>>
    getObservableUploadAttachment(final List<AttachmentViewModel> attachmentList) {
        return new Func1<CreateResoRequestDomain, Observable<List<UploadDomain>>>() {
            @Override
            public Observable<List<UploadDomain>> call(final CreateResoRequestDomain createResoRequestDomain) {
                return Observable.from(attachmentList)
                        .flatMap(new Func1<AttachmentViewModel, Observable<UploadDomain>>() {
                            @Override
                            public Observable<UploadDomain> call(AttachmentViewModel attachmentViewModel) {
                                if (attachmentViewModel.isImage()) {
                                    return uploadUseCase.createObservable(
                                            UploadUseCase.getParam(
                                                    createResoRequestDomain,
                                                    attachmentViewModel.getAttachmentId(),
                                                    attachmentViewModel.getFileLoc()
                                            ));
                                } else {
                                    return uploadVideoUseCase.createObservable(
                                            UploadUseCase.getParam(
                                                    createResoRequestDomain,
                                                    attachmentViewModel.getAttachmentId(),
                                                    attachmentViewModel.getFileLoc()
                                            ));
                                }
                            }
                        }).toList();
            }
        };
    }

    private Func1<List<UploadDomain>, Observable<CreateResoRequestDomain>>
    addListAttachmentUploadToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return new Func1<List<UploadDomain>, Observable<CreateResoRequestDomain>>() {
            @Override
            public Observable<CreateResoRequestDomain> call(List<UploadDomain> uploadImageDomains) {
                createResoRequestDomain.setUploadDomain(uploadImageDomains);
                return Observable.just(createResoRequestDomain);
            }
        };
    }

    private Func1<CreateResoRequestDomain, Observable<CreateSubmitDomain>>
    getObservableCreateSubmitReso(CreateResoRequestDomain createResoRequestDomain) {
        return new Func1<CreateResoRequestDomain, Observable<CreateSubmitDomain>>() {
            @Override
            public Observable<CreateSubmitDomain> call(CreateResoRequestDomain createResoRequestDomain) {
                return createSubmitUseCase.createObservable(
                        CreateSubmitUseCase.createResoSubmitParams(createResoRequestDomain));
            }
        };
    }

    private Func1<CreateSubmitDomain, Observable<CreateResoRequestDomain>>
    addCreateSubmitToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return new Func1<CreateSubmitDomain, Observable<CreateResoRequestDomain>>() {
            @Override
            public Observable<CreateResoRequestDomain> call(CreateSubmitDomain createSubmitDomain) {
                createResoRequestDomain.setCreateSubmitDomain(createSubmitDomain);
                return Observable.just(createResoRequestDomain);
            }
        };
    }

    private Func1<CreateResoRequestDomain, Observable<CreateSubmitDomain>> mappingResultToDomain() {
        return new Func1<CreateResoRequestDomain, Observable<CreateSubmitDomain>>() {
            @Override
            public Observable<CreateSubmitDomain> call(
                    CreateResoRequestDomain createResoRequestDomain) {
                return Observable.just(createResoRequestDomain.getCreateSubmitDomain());
            }
        };
    }

    public RequestParams createResoWithAttachmentRequestParams(ResultViewModel resultViewModel) {
        return CreateValidateUseCase.createResoValidateParams(resultViewModel);
    }

}
