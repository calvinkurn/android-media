package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import android.text.TextUtils;

import com.drew.lang.annotations.Nullable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewRequestModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.UploadImageDomain;

import java.util.ArrayList;
import java.util.List;

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
                .flatMap(getObservableUploadImages(getListImage(requestParams)))
                .flatMap(getObservableSubmitReview())
                .flatMap(mappingResultToDomain());
    }

    @Override
    protected Observable<SendReviewRequestModel> getObservableValidateReview(
            RequestParams requestParams, SendReviewRequestModel sendReviewRequestModel) {
        return editReviewValidateUseCase.createObservable(requestParams)
                .flatMap(addValidateResultToRequestModel(sendReviewRequestModel));
    }

    protected Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>
    getObservableSubmitReview() {
        return new Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(SendReviewRequestModel sendReviewRequestModel) {
                if (TextUtils.isEmpty(sendReviewRequestModel.getPostKey())) {
                    return Observable.just(sendReviewRequestModel);
                } else {
                    return editReviewSubmitUseCase.createObservable(
                            SendReviewSubmitUseCase.getParam(sendReviewRequestModel))
                            .flatMap(addSubmitImageResultToRequestModel(sendReviewRequestModel));
                }
            }
        };
    }

    @Override
    protected Func1<List<UploadImageDomain>, Observable<SendReviewRequestModel>>
    addListImageUploadToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<List<UploadImageDomain>, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(@Nullable List<UploadImageDomain>
                                                                   uploadImageDomains) {
                if (!TextUtils.isEmpty(sendReviewRequestModel.getPostKey())) {
                    for (int i = 0; i < uploadImageDomains.size(); i++) {
                        sendReviewRequestModel.getListUpload().get(i).setPicObj(uploadImageDomains
                                .get(i).getPicObj());
                        sendReviewRequestModel.getListUpload().get(i).setPicSrc(uploadImageDomains
                                .get(i).getPicSrc());
                    }
                }
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    @Override
    protected Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>> getObservableUploadImages(final List<ImageUpload> listImage) {
        return new Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(final SendReviewRequestModel sendReviewRequestModel) {
                return Observable.from(listImage)
                        .flatMap(new Func1<ImageUpload, Observable<UploadImageDomain>>() {
                            @Override
                            public Observable<UploadImageDomain> call(ImageUpload imageUpload) {
                                if (TextUtils.isEmpty(sendReviewRequestModel.getPostKey())) {
                                    return Observable.just(null);
                                } else if (imageUpload.getFileLoc() == null
                                        && !TextUtils.isEmpty(imageUpload.getPicSrc())) {
                                    return Observable.just(new UploadImageDomain(imageUpload
                                            .getPicObj(), imageUpload.getPicSrc()));
                                } else {
                                    return uploadImageUseCase.createObservable(
                                            UploadImageUseCase.getParam(
                                                    sendReviewRequestModel,
                                                    imageUpload.getImageId(),
                                                    imageUpload.getFileLoc()
                                            ));
                                }
                            }
                        }).toList()
                        .flatMap(addListImageUploadToRequestModel(sendReviewRequestModel));
            }
        };
    }
}
