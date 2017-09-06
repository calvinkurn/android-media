package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewRequestModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.UploadImageDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewUseCase extends UseCase<SendReviewDomain> {

    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";

    public static final String IMAGE = "image";

    private static final String PARAM_LIST_IMAGE = "LIST_IMAGE";
    private static final String PARAM_LIST_DELETED_IMAGE = "PARAM_LIST_DELETED_IMAGE";

    private final SendReviewValidateUseCase sendReviewValidateUseCase;
    private final GenerateHostUseCase generateHostUseCase;
    private final UploadImageUseCase uploadImageUseCase;
    private final SendReviewSubmitUseCase sendReviewSubmitUseCase;

    public SendReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             SendReviewValidateUseCase sendReviewValidateUseCase,
                             GenerateHostUseCase generateHostUseCase,
                             UploadImageUseCase uploadImageUseCase,
                             SendReviewSubmitUseCase sendReviewSubmitUseCase) {
        super(threadExecutor, postExecutionThread);
        this.sendReviewValidateUseCase = sendReviewValidateUseCase;
        this.generateHostUseCase = generateHostUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.sendReviewSubmitUseCase = sendReviewSubmitUseCase;
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

    private Func1<SendReviewSubmitDomain, Observable<SendReviewRequestModel>>
    addSubmitImageResultToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<SendReviewSubmitDomain, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(SendReviewSubmitDomain sendReviewSubmitDomain) {
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    private Func1<SendReviewRequestModel, Observable<SendReviewSubmitDomain>> getObservableSubmitReview(SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<SendReviewRequestModel, Observable<SendReviewSubmitDomain>>() {
            @Override
            public Observable<SendReviewSubmitDomain> call(SendReviewRequestModel sendReviewRequestModel) {
                return sendReviewSubmitUseCase.createObservable(
                        SendReviewSubmitUseCase.getParam(sendReviewRequestModel));
            }
        };
    }

    private RequestParams getParamSendReviewValidation(RequestParams requestParams) {
        return SendReviewValidateUseCase.getParamWithImage(
                requestParams.getString(SendReviewValidateUseCase.PARAM_REVIEW_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_PRODUCT_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_REPUTATION_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_SHOP_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_RATING, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_REVIEW_MESSAGE, ""),
                (ArrayList<ImageUpload>) requestParams.getObject(PARAM_LIST_IMAGE),
                (List<ImageUpload>) requestParams.getObject(PARAM_LIST_DELETED_IMAGE)
        );
    }

    private Func1<List<UploadImageDomain>, Observable<SendReviewRequestModel>>
    addListImageUploadToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<List<UploadImageDomain>, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(List<UploadImageDomain> uploadImageDomains) {
                for (int i = 0; i < uploadImageDomains.size(); i++) {
                    sendReviewRequestModel.getListUpload().get(i).setPicObj(uploadImageDomains
                            .get(i).getPicObj());
                    sendReviewRequestModel.getListUpload().get(i).setPicSrc(uploadImageDomains
                            .get(i).getPicSrc());
                }
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    private List<ImageUpload> getListImage(RequestParams requestParams) {
        return (List<ImageUpload>) requestParams.getObject(PARAM_LIST_IMAGE);
    }

    private Func1<SendReviewRequestModel, Observable<List<UploadImageDomain>>>
    getObservableUploadImages(final List<ImageUpload> listImage) {
        return new Func1<SendReviewRequestModel, Observable<List<UploadImageDomain>>>() {
            @Override
            public Observable<List<UploadImageDomain>> call(final SendReviewRequestModel sendReviewRequestModel) {
                return Observable.from(listImage)
                        .flatMap(new Func1<ImageUpload, Observable<UploadImageDomain>>() {
                            @Override
                            public Observable<UploadImageDomain> call(ImageUpload imageUpload) {
                                return uploadImageUseCase.createObservable(
                                        UploadImageUseCase.getParam(
                                                sendReviewRequestModel,
                                                imageUpload.getImageId(),
                                                imageUpload.getFileLoc()
                                        ));
                            }
                        }).toList();
            }
        };
    }

    private Func1<SendReviewRequestModel, Observable<GenerateHostDomain>>
    getObservableGenerateHost(final RequestParams param) {
        return new Func1<SendReviewRequestModel, Observable<GenerateHostDomain>>() {
            @Override
            public Observable<GenerateHostDomain> call(SendReviewRequestModel sendReviewRequestModel) {
                return generateHostUseCase.createObservable(param);
            }
        };
    }

    private Func1<GenerateHostDomain, Observable<SendReviewRequestModel>>
    addGenerateHostResultToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<GenerateHostDomain, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(GenerateHostDomain generateHostDomain) {
                sendReviewRequestModel.setUploadHost(generateHostDomain.getUploadHost());
                sendReviewRequestModel.setServerId(generateHostDomain.getServerId());
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    private Func1<SendReviewRequestModel, Observable<SendReviewDomain>> mappingResultToDomain() {
        return new Func1<SendReviewRequestModel, Observable<SendReviewDomain>>() {
            @Override
            public Observable<SendReviewDomain> call(
                    SendReviewRequestModel sendReviewRequestModel) {
                SendReviewDomain sendReviewDomain =
                        new SendReviewDomain(false);
                return Observable.just(sendReviewDomain);
            }
        };
    }

    private Observable<SendReviewRequestModel> getObservableValidateReview(
            RequestParams requestParams, final SendReviewRequestModel sendReviewRequestModel) {
        return sendReviewValidateUseCase.createObservable(requestParams)
                .flatMap(addValidateResultToRequestModel(sendReviewRequestModel));
    }

    private Func1<SendReviewValidateDomain, Observable<SendReviewRequestModel>>
    addValidateResultToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<SendReviewValidateDomain, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(
                    SendReviewValidateDomain sendReviewValidateDomain) {
                sendReviewRequestModel.setPostKey(sendReviewValidateDomain.getPostKey());
                sendReviewRequestModel.setReviewId(sendReviewValidateDomain.getReviewId());
                sendReviewRequestModel.setValidateSuccess(sendReviewValidateDomain.getIsSuccess() == 1);
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    public static RequestParams getParam(String reviewId,
                                         String productId,
                                         String reputationId,
                                         String shopId,
                                         String rating,
                                         String reviewMessage,
                                         ArrayList<ImageUpload> list,
                                         List<ImageUpload> deletedList) {
        RequestParams params = RequestParams.create();
        params.getParameters().putAll(SendReviewValidateUseCase.getParamWithImage(reviewId, productId,
                reputationId, shopId, rating, reviewMessage, list, deletedList).getParameters());
        params.putObject(PARAM_LIST_IMAGE, list);
        params.putObject(PARAM_LIST_DELETED_IMAGE, deletedList);
        return params;
    }

}
