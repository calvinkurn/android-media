package com.tokopedia.inbox.rescenter.discussion.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.AttachmentData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionData;
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
import rx.functions.Func2;

/**
 * Created by hangnadi on 6/9/17.
 */

public class SendDiscussionV2UseCase extends UseCase<DiscussionItemViewModel> {

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

    private static final String PARAM_IS_HAS_ATTACHMENT = "PARAM_IS_HAS_ATTACHMENT";
    private static final String PARAM_TOKEN = "PARAM_TOKEN";

    private final GenerateHostUseCase generateHostUseCase;
    private final ReplyDiscussionValidationUseCase replyValidationUseCase;
    private final UploadImageV2UseCase uploadImageUseCase;
    private final UploadVideoUseCase uploadVideoUseCase;
    private final CreatePictureUseCase createPictureUseCase;
    private final ReplyDiscussionSubmitUseCase replySubmitUseCase;
    private ActionDiscussionModel actionDiscussionModel;

    public SendDiscussionV2UseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   GenerateHostUseCase generateHostUseCase,
                                   ReplyDiscussionValidationUseCase replyValidationUseCase,
                                   UploadImageV2UseCase uploadImageUseCase,
                                   UploadVideoUseCase uploadVideoUseCase,
                                   CreatePictureUseCase createPictureUseCase,
                                   ReplyDiscussionSubmitUseCase replySubmitUseCase) {

        super(threadExecutor, postExecutionThread);
        this.generateHostUseCase = generateHostUseCase;
        this.replyValidationUseCase = replyValidationUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.uploadVideoUseCase = uploadVideoUseCase;
        this.createPictureUseCase = createPictureUseCase;
        this.replySubmitUseCase = replySubmitUseCase;
    }

    public void setActionDiscussionModel(ActionDiscussionModel actionDiscussionModel) {
        this.actionDiscussionModel = actionDiscussionModel;
    }

    @Override
    public Observable<DiscussionItemViewModel> createObservable(RequestParams requestParams) {
        if (actionDiscussionModel == null) {
            return Observable.empty();
        }
        return Observable.just(actionDiscussionModel)
                .flatMap(new Func1<ActionDiscussionModel, Observable<ActionDiscussionModel>>() {
                    @Override
                    public Observable<ActionDiscussionModel> call(ActionDiscussionModel model) {
                        return getReplyValidation(model);
                    }
                })
                .flatMap(new Func1<ActionDiscussionModel, Observable<ActionDiscussionModel>>() {
                    @Override
                    public Observable<ActionDiscussionModel> call(ActionDiscussionModel model) {
                        if (model.isHasAttachment()) {
                            return getGenerateHost(model);
                        } else {
                            return Observable.just(model);
                        }
                    }
                })
                .flatMap(new Func1<ActionDiscussionModel, Observable<ActionDiscussionModel>>() {
                    @Override
                    public Observable<ActionDiscussionModel> call(ActionDiscussionModel model) {
                        if (model.isHasAttachment()) {
                            return getUploadAttachment(model);
                        } else {
                            return Observable.just(model);
                        }
                    }
                })
                .flatMap(new Func1<ActionDiscussionModel, Observable<DiscussionItemViewModel>>() {
                    @Override
                    public Observable<DiscussionItemViewModel> call(ActionDiscussionModel model) {
                        if (model.isHasAttachment()) {
                            return getReplySubmit(model);
                        } else {
                            return Observable.just(model.getReplyDiscussionData());
                        }
                    }
                });
    }

    private Observable<ActionDiscussionModel> getReplyValidation(ActionDiscussionModel model) {
        RequestParams params = RequestParams.create();
        params.putString(SendDiscussionUseCase.PARAM_MESSAGE, model.getMessage());
        params.putObject(SendDiscussionUseCase.PARAM_RESOLUTION_ID, model.getResolutionId());
        params.putInt(SendDiscussionUseCase.PARAM_FLAG_RECEIVED, model.getFlagReceived());

        if (model.getAttachment() != null && model.getAttachment().size() > 0) {
            params.putObject(SendDiscussionUseCase.PARAM_ATTACHMENT, model.getAttachment());
        }

        return Observable.zip(
                Observable.just(model),
                replyValidationUseCase.createObservable(params),
                new Func2<ActionDiscussionModel, ReplyDiscussionValidationModel, ActionDiscussionModel>() {
                    @Override
                    public ActionDiscussionModel call(ActionDiscussionModel model, ReplyDiscussionValidationModel domainModel) {
                        if (model.isHasAttachment()) {
                            model.setPostKey(domainModel.getPostKey());
                            model.setToken(domainModel.getToken());
                        } else {
                            model.setReplyDiscussionData(
                                    mappingValidationViewModel(domainModel.getReplyDiscussionData())
                            );
                        }
                        return model;
                    }
                }
        );
    }

    private DiscussionItemViewModel mappingValidationViewModel(ReplyDiscussionData replyDiscussionData) {
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

    private Observable<ActionDiscussionModel> getGenerateHost(ActionDiscussionModel model) {
        return Observable.zip(
                Observable.just(model),
                generateHostUseCase.createObservable(getGenerateHostParam()),
                new Func2<ActionDiscussionModel, GenerateHostModel, ActionDiscussionModel>() {
                    @Override
                    public ActionDiscussionModel call(ActionDiscussionModel model, GenerateHostModel domainModel) {
                        model.setServerId(domainModel.getGenerateHostData().getServerId());
                        model.setUploadHost(domainModel.getGenerateHostData().getUploadHost());
                        return model;
                    }
                }
        );
    }

    private RequestParams getGenerateHostParam() {
        RequestParams generateHostParams = RequestParams.create();
        generateHostParams.putInt(GenerateHostUseCase.PARAM_SERVER_LANGUAGE, GenerateHostUseCase.STATIC_GOLANG_VALUE);
        return generateHostParams;
    }

    private Observable<ActionDiscussionModel> getUploadAttachment(ActionDiscussionModel model) {
        return Observable.zip(
                Observable.just(model),
                getObservableUploadAttachment(model),
                new Func2<ActionDiscussionModel, List<UploadImageModel>, ActionDiscussionModel>() {
                    @Override
                    public ActionDiscussionModel call(ActionDiscussionModel model, List<UploadImageModel> listUploadedAttachment) {
                        model.setUploadedFile(listUploadedAttachment);
                        return model;
                    }
                }
        );
    }

    private Observable<List<UploadImageModel>> getObservableUploadAttachment(final ActionDiscussionModel model) {
        return Observable.from(model.getAttachment())
                .flatMap(new Func1<AttachmentViewModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(AttachmentViewModel attachment) {
                        if (attachment.getFileType() == AttachmentViewModel.FILE_VIDEO) {
                            RequestParams uploadVideoParams = RequestParams.create();
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_FILE_TO_UPLOAD, attachment.getFileLoc());
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_FILE_NAME, attachment.getFileLoc().substring(attachment.getFileLoc().lastIndexOf("/")+1));
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_SERVER_ID, model.getServerId());
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_URL, model.getUploadHost());
                            return uploadVideoUseCase.createObservable(uploadVideoParams);
                        } else {
                            RequestParams uploadImageParams = RequestParams.create();
                            uploadImageParams.putString(UploadImageUseCase.PARAM_SERVER_ID, model.getServerId());
                            uploadImageParams.putString(UploadImageUseCase.PARAM_URL, model.getUploadHost());
                            uploadImageParams.putString(UploadImageUseCase.PARAM_FILE_TO_UPLOAD, attachment.getFileLoc());
                            return uploadImageUseCase.createObservable(uploadImageParams);
                        }
                    }
                })
                .toList();
    }

    private Observable<DiscussionItemViewModel> getReplySubmit(ActionDiscussionModel model) {
        return replySubmitUseCase.createObservable(getReplySubmitParams(model))
                .flatMap(new Func1<ReplySubmitModel, Observable<DiscussionItemViewModel>>() {
                    @Override
                    public Observable<DiscussionItemViewModel> call(ReplySubmitModel domainModel) {
                        ReplySubmitData data = domainModel.getReplySubmitData();
                        DiscussionItemViewModel viewModel = new DiscussionItemViewModel();
                        viewModel.setMessageReplyTimeFmt(formatTime(data.getCreateTimeWib()));
                        viewModel.setMessage(data.getRemarkStr());
                        viewModel.setUserLabel(data.getUserLabel());
                        viewModel.setUserLabelId(data.getUserLabelId());
                        viewModel.setUserName(data.getUserName());
                        viewModel.setAttachment(mappingSubmitAttachment(data.getAttachment()));
                        viewModel.setConversationId(String.valueOf(data.getConversationId()));
                        viewModel.setMessageCreateBy(SessionHandler.getLoginID(MainApplication.getAppContext()));
                        return Observable.just(viewModel);
                    }
                });
    }

    private RequestParams getReplySubmitParams(ActionDiscussionModel model) {
        RequestParams params = RequestParams.create();
        params.putString(ReplyDiscussionSubmitUseCase.PARAM_RESOLUTION_ID, model.getResolutionId());
        params.putString(ReplyDiscussionSubmitUseCase.PARAM_POST_KEY, model.getPostKey());
        params.putString(ReplyDiscussionSubmitUseCase.PARAM_FILE_UPLOADED, generateFileUploaded(model));
        return params;
    }

    private String generateFileUploaded(ActionDiscussionModel model) {
        return null;
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

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        this.generateHostUseCase.unsubscribe();
        this.replyValidationUseCase.unsubscribe();
        this.uploadImageUseCase.unsubscribe();
        this.uploadVideoUseCase.unsubscribe();
        this.createPictureUseCase.unsubscribe();
        this.replySubmitUseCase.unsubscribe();
    }

}
