package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyDiscussionDomainData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.AttachmentData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionValidationUseCase.PARAM_PHOTOS;

/**
 * Created by nisie on 3/30/17.
 */

public class SendDiscussionUseCase extends UseCase<DiscussionItemViewModel> {

    public static final String PARAM_MESSAGE = "PARAM_MESSAGE";
    public static final String PARAM_ATTACHMENT = "PARAM_ATTACHMENT";
    public static final String PARAM_RESOLUTION_ID = "PARAM_RESOLUTION_ID";
    public static final String PARAM_FLAG_RECEIVED = "PARAM_FLAG_RECEIVED";
    private static final String IS_SUCCESS_VALIDATION = "IS_SUCCESS_VALIDATION";

    private static final String PARAM_SERVER_ID = "PARAM_SERVER_ID";
    private static final String PARAM_UPLOAD_HOST = "PARAM_UPLOAD_HOST";
    private static final String PARAM_ATTACHMENT_STRING = "PARAM_ATTACHMENT_STRING";
    private static final String PARAM_FILE_UPLOADED = "PARAM_FILE_UPLOADED";
    private static final String PARAM_POST_KEY = "PARAM_POST_KEY";
    private static final String PARAM_REPLY_DATA = "PARAM_REPLY_DATA";


    private final GenerateHostUseCase generateHostUseCase;
    private final ReplyDiscussionValidationUseCase replyDiscussionValidationUseCase;
    private final UploadImageUseCase uploadImageUseCase;
    private final CreatePictureUseCase createPictureUseCase;
    private final ReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase;

    public SendDiscussionUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 GenerateHostUseCase generateHostUseCase,
                                 ReplyDiscussionValidationUseCase replyDiscussionValidationUseCase,
                                 UploadImageUseCase uploadImageUseCase,
                                 CreatePictureUseCase createPictureUseCase,
                                 ReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase) {

        super(threadExecutor, postExecutionThread);
        this.generateHostUseCase = generateHostUseCase;
        this.replyDiscussionValidationUseCase = replyDiscussionValidationUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.createPictureUseCase = createPictureUseCase;
        this.replyDiscussionSubmitUseCase = replyDiscussionSubmitUseCase;
    }

    @Override
    public Observable<DiscussionItemViewModel> createObservable(final RequestParams requestParams) {
        final ActionDiscussionModel actionDiscussionModel = new ActionDiscussionModel();
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(RequestParams requestParams) {
                        if (isHasAttachment(requestParams))
                            return generateHost(getGenerateHostParam(requestParams), requestParams);
                        else
                            return Observable.just(requestParams);
                    }
                })
                .flatMap(new Func1<RequestParams, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(RequestParams requestParams) {
                        if (isHasAttachment(requestParams))
                            return uploadFiles(getUploadFileParam(requestParams),
                                    requestParams);
                        else
                            return Observable.just(requestParams);
                    }
                })
                .flatMap(new Func1<RequestParams, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(RequestParams requestParams) {
                        return replyConversationValidation(
                                getReplyValidationParam(requestParams),
                                requestParams);
                    }
                })
                .flatMap(new Func1<RequestParams, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(RequestParams requestParams) {
                        if (isHasAttachment(requestParams))
                            return createResolutionPicture(
                                    getCreatePictureParam(requestParams),
                                    requestParams);
                        else
                            return Observable.just(requestParams);
                    }
                })
                .flatMap(new Func1<RequestParams, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(RequestParams requestParams) {
                        if (isHasAttachment(requestParams))
                            return replyConversationSubmit(
                                    getSubmitParam(requestParams),
                                    requestParams);
                        else
                            return Observable.just(requestParams);
                    }
                })
                .flatMap(new Func1<RequestParams, Observable<DiscussionItemViewModel>>() {
                    @Override
                    public Observable<DiscussionItemViewModel> call(RequestParams requestParams) {
                        return Observable.just(
                                ((DiscussionItemViewModel) requestParams.getObject(PARAM_REPLY_DATA)));


                    }
                });
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        this.generateHostUseCase.unsubscribe();
        this.replyDiscussionValidationUseCase.unsubscribe();
        this.uploadImageUseCase.unsubscribe();
        this.createPictureUseCase.unsubscribe();
        this.replyDiscussionSubmitUseCase.unsubscribe();
    }

    private Observable<RequestParams> createResolutionPicture(RequestParams createPictureParam,
                                                              final RequestParams requestParams) {
        return createPictureUseCase.createObservable(createPictureParam)
                .flatMap(new Func1<CreatePictureModel, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(CreatePictureModel createPictureModel) {
                        requestParams.putString(PARAM_FILE_UPLOADED,
                                createPictureModel.getUploadImageData().getFileUploaded());
                        return Observable.just(requestParams);
                    }
                });
    }

    public static RequestParams getSendReplyParams(String resolutionId, String message, List<AttachmentViewModel> attachmentList) {
        RequestParams params = RequestParams.create();
        params.putString(SendDiscussionV2UseCase.PARAM_RESOLUTION_ID, resolutionId);
        params.putString(PARAM_MESSAGE, message);
        if (attachmentList != null && attachmentList.size() > 0) {
            params.putObject(SendDiscussionV2UseCase.PARAM_ATTACHMENT, attachmentList);
        }
        return params;
    }

    private RequestParams getCreatePictureParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(CreatePictureUseCase.PARAM_ATTACHMENT_STRING,
                requestParams.getString(PARAM_ATTACHMENT_STRING, ""));
        params.putString(CreatePictureUseCase.PARAM_SERVER_ID,
                requestParams.getString(PARAM_SERVER_ID, ""));
        params.putString(UploadImageUseCase.PARAM_URL,
                requestParams.getString(PARAM_UPLOAD_HOST, ""));
        return params;
    }

    private Observable<RequestParams> replyConversationSubmit(RequestParams submitParam,
                                                              final RequestParams requestParams) {
        return replyDiscussionSubmitUseCase.createObservable(submitParam)
                .flatMap(new Func1<ReplySubmitModel, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(ReplySubmitModel replySubmitModel) {
                        requestParams.putObject(PARAM_REPLY_DATA, mappingSubmitViewModel(replySubmitModel.getReplySubmitData()));
                        return Observable.just(requestParams);
                    }
                });
    }

    private DiscussionItemViewModel mappingSubmitViewModel(ReplySubmitData replySubmitData) {
        DiscussionItemViewModel viewModel = new DiscussionItemViewModel();
        viewModel.setMessageReplyTimeFmt(formatTime(replySubmitData.getCreateTimeWib()));
        viewModel.setMessage(replySubmitData.getRemarkStr());
        viewModel.setUserLabel(replySubmitData.getUserLabel());
        viewModel.setUserLabelId(replySubmitData.getUserLabelId());
        viewModel.setUserName(replySubmitData.getUserName());
        viewModel.setAttachment(mappingSubmitAttachment(replySubmitData.getAttachment()));
        viewModel.setConversationId(String.valueOf(replySubmitData.getConversationId()));
        viewModel.setMessageCreateBy(SessionHandler.getLoginID(MainApplication.getAppContext()));
        return viewModel;
    }

    private List<AttachmentViewModel> mappingSubmitAttachment(List<AttachmentData> listAttachment) {
        List<AttachmentViewModel> list = new ArrayList<>();
        for (AttachmentData attachmentData : listAttachment) {
            AttachmentViewModel attachmentViewModel = new AttachmentViewModel();
            attachmentViewModel.setImgThumb(attachmentData.getFileUrl());
            attachmentViewModel.setImgLarge(attachmentData.getRealFileUrl());
            list.add(attachmentViewModel);
        }

        return list;
    }

    private RequestParams getSubmitParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(ReplyDiscussionSubmitUseCase.PARAM_FILE_UPLOADED,
                requestParams.getString(PARAM_FILE_UPLOADED, ""));
        params.putString(ReplyDiscussionSubmitUseCase.PARAM_POST_KEY,
                requestParams.getString(PARAM_POST_KEY, ""));
        params.putString(ReplyDiscussionSubmitUseCase.PARAM_RESOLUTION_ID,
                requestParams.getString(PARAM_RESOLUTION_ID, ""));
        return params;
    }

    private Observable<RequestParams> uploadFiles(final RequestParams uploadFileParam, final RequestParams requestParams) {
        return Observable.from(getListImage(uploadFileParam))
                .flatMap(new Func1<AttachmentViewModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(AttachmentViewModel attachment) {
                        uploadFileParam.putString(UploadImageUseCase.PARAM_FILE_TO_UPLOAD, attachment.getFileLoc());
                        return uploadImageUseCase.createObservable(uploadFileParam);
                    }
                }).toList()
                .flatMap(new Func1<List<UploadImageModel>, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(List<UploadImageModel> uploadImageModels) {
                        for (int i = 0; i < uploadImageModels.size(); i++) {
                            String attachmentString = requestParams.getString(PARAM_ATTACHMENT_STRING, "");
                            if (i == uploadImageModels.size() - 1)
                                attachmentString += uploadImageModels.get(i).getUploadImageData().getImageUrl();
                            else
                                attachmentString += uploadImageModels.get(i).getUploadImageData().getImageUrl() + "~";

                            requestParams.putString(PARAM_ATTACHMENT_STRING, attachmentString);
                        }
                        return Observable.just(requestParams);
                    }
                });
    }

    private List<AttachmentViewModel> getListImage(RequestParams uploadFileParam) {
        return (List<AttachmentViewModel>) uploadFileParam.getObject(PARAM_ATTACHMENT);
    }

    private Observable<RequestParams> generateHost(RequestParams generateHostParam, final RequestParams requestParams) {
        return generateHostUseCase.createObservable(generateHostParam)
                .flatMap(new Func1<GenerateHostModel, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(GenerateHostModel generateHostModel) {
                        requestParams.putString(PARAM_SERVER_ID, generateHostModel.getGenerateHostData().getServerId());
                        requestParams.putString(PARAM_UPLOAD_HOST, generateHostModel.getGenerateHostData().getUploadHost());
                        return Observable.just(requestParams);
                    }
                });
    }

    private RequestParams getUploadFileParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(UploadImageUseCase.PARAM_SERVER_ID, requestParams.getString(PARAM_SERVER_ID, ""));
        params.putString(UploadImageUseCase.PARAM_URL, requestParams.getString(PARAM_UPLOAD_HOST, ""));
        params.putObject(PARAM_ATTACHMENT, requestParams.getObject(PARAM_ATTACHMENT));
        return params;
    }

    private RequestParams getGenerateHostParam(RequestParams requestParams) {
        RequestParams generateHostParams = RequestParams.create();
        generateHostParams.putInt(GenerateHostUseCase.PARAM_SERVER_LANGUAGE, GenerateHostUseCase.STATIC_GOLANG_VALUE);
        return generateHostParams;
    }


    private boolean isHasAttachment(RequestParams requestParams) {
        return requestParams.getObject(PARAM_ATTACHMENT) != null;
    }

    private Observable<RequestParams> replyConversationValidation(RequestParams replyValidationParam,
                                                                  final RequestParams requestParams) {
        return replyDiscussionValidationUseCase.createObservable(replyValidationParam)
                .flatMap(new Func1<ReplyDiscussionValidationModel, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(ReplyDiscussionValidationModel result) {
                        if (isHasAttachment(requestParams)) {
                            requestParams.putString(PARAM_POST_KEY,
                                    result.getReplyDiscussionData().getPostKey());
                        } else {
                            requestParams.putObject(PARAM_REPLY_DATA,
                                    mappingValidationViewModel(result.getReplyDiscussionData()));
                        }

                        requestParams.putBoolean(IS_SUCCESS_VALIDATION, result.isSuccess());
                        return Observable.just(requestParams);
                    }
                });
    }

    private DiscussionItemViewModel mappingValidationViewModel(ReplyDiscussionDomainData replyDiscussionData) {
        DiscussionItemViewModel viewModel = new DiscussionItemViewModel();
        viewModel.setMessageCreateBy(SessionHandler.getLoginID(MainApplication.getAppContext()));
        viewModel.setMessage(replyDiscussionData.getRemarkStr());
        viewModel.setMessageReplyTimeFmt(formatTime(replyDiscussionData.getCreateTimeWib()));
        viewModel.setMessage(replyDiscussionData.getRemarkStr());
        viewModel.setUserLabel(replyDiscussionData.getUserLabel());
        viewModel.setUserLabelId(replyDiscussionData.getUserLabelId());
        viewModel.setUserName(replyDiscussionData.getUserName());
        viewModel.setConversationId(String.valueOf(replyDiscussionData.getConversationId()));
        return viewModel;
    }

    private String formatTime(String createTimeOld) {
        Locale id = new Locale("in", "ID");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy hh:mm", id);
        SimpleDateFormat newSdf = new SimpleDateFormat(DiscussionItemViewModel.DISCUSSION_DATE_TIME_FORMAT, id);
        String createTimeNew;
        try {
            createTimeNew = newSdf.format(sdf.parse(createTimeOld.replace("WIB", "")));
        } catch (ParseException e) {
            createTimeNew = createTimeOld;
        }
        return createTimeNew;
    }

    private RequestParams getReplyValidationParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        if (isHasAttachment(requestParams)) {
            params.putString(PARAM_PHOTOS, requestParams.getString(PARAM_ATTACHMENT_STRING, ""));
            params.putString(PARAM_SERVER_ID, requestParams.getString(PARAM_SERVER_ID, ""));
        }
        params.putString(ReplyDiscussionValidationUseCase.PARAM_REPLY_MSG, requestParams.getString(PARAM_MESSAGE, ""));
        params.putString(ReplyDiscussionValidationUseCase.PARAM_RESOLUTION_ID, requestParams.getString(PARAM_RESOLUTION_ID, ""));
        params.putString(ReplyDiscussionValidationUseCase.PARAM_EDIT_SOL_FLAG, ReplyDiscussionValidationUseCase.DEFAULT_EDIT_SOL_FLAG);
        params.putInt(ReplyDiscussionValidationUseCase.PARAM_FLAG_RECEIVED, requestParams.getInt(PARAM_FLAG_RECEIVED, 0));
        params.putString(ReplyDiscussionValidationUseCase.PARAM_REFUND_AMOUNT, ReplyDiscussionValidationUseCase.DEFAULT_REFUND_AMOUNT);
        params.putString(ReplyDiscussionValidationUseCase.PARAM_SOLUTION, ReplyDiscussionValidationUseCase.DEFAULT_SOLUTION);
        params.putString(ReplyDiscussionValidationUseCase.PARAM_TROUBLE_TYPE, ReplyDiscussionValidationUseCase.DEFAULT_TROUBLE_TYPE);
        return params;
    }
}