package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewSubmitUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewRequestModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewUseCase extends SendReviewUseCase {


    private final EditReviewValidateUseCase editReviewValidateUseCase;
    private final EditReviewSubmitUseCase editReviewSubmitUseCase;

    public EditReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             EditReviewValidateUseCase editReviewValidateUseCase,
                             GenerateHostUseCase generateHostUseCase,
                             UploadImageUseCase uploadImageUseCase,
                             EditReviewSubmitUseCase editReviewSubmitUseCase) {
        super(threadExecutor, postExecutionThread,
                generateHostUseCase, uploadImageUseCase);
        this.editReviewValidateUseCase = editReviewValidateUseCase;
        this.editReviewSubmitUseCase = editReviewSubmitUseCase;
    }

    @Override
    public Observable<SendReviewDomain> createObservable(RequestParams requestParams) {
        final SendReviewRequestModel sendReviewRequestModel = new
                SendReviewRequestModel();
        sendReviewRequestModel.setListUpload((ArrayList<ImageUpload>) requestParams.getObject(PARAM_LIST_IMAGE));
        return getObservableValidateReview(getParamSendReviewValidation(requestParams),
                sendReviewRequestModel)
                .flatMap(getObservableGenerateHost(GenerateHostUseCase.getParam()))
                .flatMap(addGenerateHostResultToRequestModel(sendReviewRequestModel))
                .flatMap(getObservableUploadImages(
                        getListImage(requestParams)))
                .flatMap(addListImageUploadToRequestModel(sendReviewRequestModel))
                .flatMap(getObservableSubmitReview(sendReviewRequestModel))
                .flatMap(addSubmitImageResultToRequestModel(sendReviewRequestModel))
                .flatMap(mappingResultToDomain());
    }

    @Override
    protected Observable<SendReviewRequestModel> getObservableValidateReview(
            RequestParams requestParams, SendReviewRequestModel sendReviewRequestModel) {
        return editReviewValidateUseCase.createObservable(requestParams)
                .flatMap(addValidateResultToRequestModel(sendReviewRequestModel));
    }

    @Override
    protected Func1<SendReviewRequestModel, Observable<SendReviewSubmitDomain>> getObservableSubmitReview(SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<SendReviewRequestModel, Observable<SendReviewSubmitDomain>>() {
            @Override
            public Observable<SendReviewSubmitDomain> call(SendReviewRequestModel sendReviewRequestModel) {
                return editReviewSubmitUseCase.createObservable(
                        SendReviewSubmitUseCase.getParam(sendReviewRequestModel));
            }
        };
    }
}
