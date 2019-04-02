package com.tokopedia.inbox.rescenter.createreso.domain.usecase;

import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.common.domain.model.UploadDomain;
import com.tokopedia.inbox.common.domain.usecase.GenerateHostUseCase;
import com.tokopedia.inbox.common.domain.usecase.UploadImageUseCase;
import com.tokopedia.inbox.common.domain.usecase.UploadVideoUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoRequestDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateValidateDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateSubmitUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateValidateUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    private UploadImageUseCase uploadUseCase;
    private CreateSubmitUseCase createSubmitUseCase;

    @Inject
    public CreateResoWithAttachmentUseCase(CreateValidateUseCase createValidateUseCase,
                                           GenerateHostUseCase generateHostUseCase,
                                           UploadImageUseCase uploadUseCase,
                                           CreateSubmitUseCase createSubmitUseCase,
                                           UploadVideoUseCase uploadVideoUseCase) {
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
        return createValidateDomain -> {
            createResoRequestDomain.setCreateValidateDomain(createValidateDomain);
            return Observable.just(createResoRequestDomain);
        };
    }

    private Func1<CreateResoRequestDomain, Observable<GenerateHostDomain>>
    getObservableGenerateHost(final RequestParams param) {
        return createResoRequestDomain -> generateHostUseCase.createObservable(param);
    }

    private Func1<GenerateHostDomain, Observable<CreateResoRequestDomain>>
    addGenerateHostResultToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return generateHostDomain -> {
            createResoRequestDomain.setGenerateHostDomain(generateHostDomain);
            return Observable.just(createResoRequestDomain);
        };
    }

    private List<AttachmentViewModel> getListAttachment(RequestParams requestParams) {
        return (List<AttachmentViewModel>) requestParams.getObject(PARAM_LIST_ATTACHMENT);
    }

    private Func1<CreateResoRequestDomain, Observable<List<UploadDomain>>>
    getObservableUploadAttachment(final List<AttachmentViewModel> attachmentList) {
        return createResoRequestDomain -> {
            if (attachmentList != null && attachmentList.size() !=0) {
                return Observable.from(attachmentList)
                        .flatMap((Func1<AttachmentViewModel, Observable<UploadDomain>>) attachmentViewModel -> {
                            if (attachmentViewModel.isImage()) {
                                return uploadUseCase.createObservable(
                                        UploadImageUseCase.getParam(
                                                createResoRequestDomain,
                                                attachmentViewModel.getAttachmentId(),
                                                attachmentViewModel.getFileLoc()
                                        ));
                            } else {
                                return uploadVideoUseCase.createObservable(
                                        UploadImageUseCase.getParam(
                                                createResoRequestDomain,
                                                attachmentViewModel.getAttachmentId(),
                                                attachmentViewModel.getFileLoc()
                                        ));
                            }
                        }).toList();
            } else {
                return Observable.just(new ArrayList<>());
            }
        };
    }

    private Func1<List<UploadDomain>, Observable<CreateResoRequestDomain>>
    addListAttachmentUploadToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return uploadImageDomains -> {
            createResoRequestDomain.setUploadDomain(uploadImageDomains);
            return Observable.just(createResoRequestDomain);
        };
    }

    private Func1<CreateResoRequestDomain, Observable<CreateSubmitDomain>>
    getObservableCreateSubmitReso(CreateResoRequestDomain createResoRequestDomain) {
        return createResoRequestDomain1 -> createSubmitUseCase.createObservable(
                CreateSubmitUseCase.createResoSubmitParams(createResoRequestDomain1));
    }

    private Func1<CreateSubmitDomain, Observable<CreateResoRequestDomain>>
    addCreateSubmitToRequestModel(final CreateResoRequestDomain createResoRequestDomain) {
        return createSubmitDomain -> {
            createResoRequestDomain.setCreateSubmitDomain(createSubmitDomain);
            return Observable.just(createResoRequestDomain);
        };
    }

    private Func1<CreateResoRequestDomain, Observable<CreateSubmitDomain>> mappingResultToDomain() {
        return createResoRequestDomain -> Observable.just(createResoRequestDomain.getCreateSubmitDomain());
    }

    public RequestParams createResoWithAttachmentRequestParams(ResultViewModel resultViewModel) {
        return CreateValidateUseCase.createResoValidateParams(resultViewModel);
    }

}
