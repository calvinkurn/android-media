package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import android.util.Log;

import com.google.gson.JsonArray;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageData;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.UploadImageModel;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.*;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyAttachmentDomainData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyDiscussionDomainData;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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

    private final GenerateHostV2UseCase generateHostUseCase;
    private final NewReplyDiscussionUseCase replyDiscussionUseCase;
    private final NewReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase;
    private final UploadImageV2UseCase uploadImageUseCase;
    private final UploadVideoUseCase uploadVideoUseCase;

    public SendDiscussionV2UseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   GenerateHostV2UseCase generateHostUseCase,
                                   NewReplyDiscussionUseCase replyDiscussionUseCase,
                                   NewReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase,
                                   UploadImageV2UseCase uploadImageUseCase,
                                   UploadVideoUseCase uploadVideoUseCase) {

        super(threadExecutor, postExecutionThread);
        this.generateHostUseCase = generateHostUseCase;
        this.replyDiscussionUseCase = replyDiscussionUseCase;
        this.replyDiscussionSubmitUseCase = replyDiscussionSubmitUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.uploadVideoUseCase = uploadVideoUseCase;
    }

    public static RequestParams getSendReplyParamsWithoutAttachment(String resolutionId, String message) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_RESOLUTION_ID, resolutionId);
        params.putString(PARAM_MESSAGE, message);
        return params;
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

    @Override
    public Observable<DiscussionItemViewModel> createObservable(RequestParams requestParams) {
        ActionDiscussionModel params = new ActionDiscussionModel();
        params.setMessage(requestParams.getString(PARAM_MESSAGE, ""));
        params.setResolutionId(requestParams.getString(PARAM_RESOLUTION_ID, ""));
        params.setFlagReceived(requestParams.getInt(PARAM_FLAG_RECEIVED, 0));
        Object object = requestParams.getObject(PARAM_ATTACHMENT);
        if (object != null) {
            params.setHasAttachment(true);
            //noinspection unchecked
            params.setAttachment((List<AttachmentViewModel>) object);
        }

        return Observable.just(params)
                .flatMap(new Func1<ActionDiscussionModel, Observable<ActionDiscussionModel>>() {
                    @Override
                    public Observable<ActionDiscussionModel> call(ActionDiscussionModel model) {
                        return getReplyResolutionStep1(model);
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
                            return getReplyResolutionStep2(model);
                        } else {
                            return Observable.just(model.getReplyDiscussionData());
                        }
                    }
                });
    }

    private Observable<ActionDiscussionModel> getReplyResolutionStep1(ActionDiscussionModel model) {
        RequestParams params = RequestParams.create();
        params.putString(NewReplyDiscussionUseCase.PARAM_REPLY_MSG, model.getMessage());
        params.putString(NewReplyDiscussionUseCase.PARAM_RESOLUTION_ID, model.getResolutionId());
        params.putInt(NewReplyDiscussionUseCase.PARAM_ATTACHMENT, model.getAttachment() != null ? model.getAttachment().size() : 0);

        return Observable.zip(
                Observable.just(model),
                replyDiscussionUseCase.createObservable(params),
                new Func2<ActionDiscussionModel, NewReplyDiscussionModel, ActionDiscussionModel>() {
                    @Override
                    public ActionDiscussionModel call(ActionDiscussionModel model, NewReplyDiscussionModel domainModel) {
                        if (model.isHasAttachment()) {
                            model.setCacheKey(domainModel.getCacheKey());
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
        viewModel.setAttachment(replyDiscussionData.getReplyAttachmentDomainData() != null ? mappingAttachmentView(replyDiscussionData.getReplyAttachmentDomainData()) : null);
        return viewModel;
    }

    private List<AttachmentViewModel> mappingAttachmentView(List<ReplyAttachmentDomainData> replyAttachmentDomainData) {
        List<AttachmentViewModel> list = new ArrayList<>();
        for (ReplyAttachmentDomainData attachments : replyAttachmentDomainData) {
            AttachmentViewModel attachmentViewModel = new AttachmentViewModel();
            attachmentViewModel.setImgThumb(attachments.getIsVideo() == 1 ? "" : attachments.getThumbnail());
            attachmentViewModel.setUrl(attachments.getFullUrl());
            attachmentViewModel.setFileType(attachments.getIsVideo() == 1 ? AttachmentViewModel.FILE_VIDEO : AttachmentViewModel.FILE_IMAGE);
            list.add(attachmentViewModel);
        }
        return list;
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
                generateHostUseCase.createObservable(RequestParams.EMPTY),
                new Func2<ActionDiscussionModel, GenerateHostModel, ActionDiscussionModel>() {
                    @Override
                    public ActionDiscussionModel call(ActionDiscussionModel model, GenerateHostModel domainModel) {
                        model.setServerId(domainModel.getGenerateHostData().getServerId());
                        model.setUploadHost(domainModel.getGenerateHostData().getUploadHost());
                        model.setToken(domainModel.getGenerateHostData().getToken());
                        return model;
                    }
                }
        );
    }

    private Observable<ActionDiscussionModel> getUploadAttachment(ActionDiscussionModel model) {
        return Observable.zip(
                Observable.just(model),
                getObservableUploadAttachment(model),
                new Func2<ActionDiscussionModel, List<UploadImageModel>, ActionDiscussionModel>() {
                    @Override
                    public ActionDiscussionModel call(ActionDiscussionModel model, List<UploadImageModel> listUploadedAttachment) {
                        model.setUploadedFile(listUploadedAttachment);
                        for (int i = 0; i < model.getAttachment().size(); i++) {
                            AttachmentViewModel viewModel = model.getAttachment().get(i);
                            viewModel.setUploadedFile(mappingFileUploaded(listUploadedAttachment.get(i).getUploadImageData()));
                        }
                        return model;
                    }
                }
        );
    }

    private AttachmentViewModel.UploadedFileViewModel mappingFileUploaded(UploadImageData data) {
        AttachmentViewModel.UploadedFileViewModel model = new AttachmentViewModel.UploadedFileViewModel();
        model.setImageUrl(data.getImageUrl());
        model.setPicObj(data.getPicObj());
        model.setPicSrc(data.getPicSrc());
        return model;
    }

    private Observable<List<UploadImageModel>> getObservableUploadAttachment(final ActionDiscussionModel model) {
        return Observable.from(model.getAttachment())
                .flatMap(new Func1<AttachmentViewModel, Observable<UploadImageModel>>() {
                    @Override
                    public Observable<UploadImageModel> call(AttachmentViewModel attachment) {
                        Log.d("hangnadi", "getObservableUploadAttachment: file type" + attachment.getFileType());
                        if (attachment.isVideo()) {
                            RequestParams uploadVideoParams = RequestParams.create();
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_FILE_TO_UPLOAD, attachment.getFileLoc());
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_FILE_NAME, attachment.getFileLoc().substring(attachment.getFileLoc().lastIndexOf("/") + 1));
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_SERVER_ID, model.getServerId());
                            uploadVideoParams.putString(UploadVideoUseCase.PARAM_URL, model.getUploadHost());
                            return uploadVideoUseCase.createObservable(uploadVideoParams);
                        } else {
                            RequestParams uploadImageParams = RequestParams.create();
                            uploadImageParams.putString(UploadImageV2UseCase.PARAM_SERVER_ID, model.getServerId());
                            uploadImageParams.putString(UploadImageV2UseCase.PARAM_URL, model.getUploadHost());
                            uploadImageParams.putString(UploadImageV2UseCase.PARAM_FILE_TO_UPLOAD, attachment.getFileLoc());
                            uploadImageParams.putString(UploadImageV2UseCase.PARAM_TOKEN, model.getToken());
                            uploadImageParams.putString(UploadImageV2UseCase.PARAM_IMAGE_ID, UUID.randomUUID().toString());
                            return uploadImageUseCase.createObservable(uploadImageParams);
                        }
                    }
                })
                .toList();
    }

    private Observable<DiscussionItemViewModel> getReplyResolutionStep2(ActionDiscussionModel model) {
        return replyDiscussionSubmitUseCase.createObservable(getReplySubmitParams(model))
                .flatMap(new Func1<NewReplyDiscussionModel, Observable<DiscussionItemViewModel>>() {
                    @Override
                    public Observable<DiscussionItemViewModel> call(NewReplyDiscussionModel domainModel) {
                        return Observable.just(mappingValidationViewModel(domainModel.getReplyDiscussionData()));
                    }
                });
    }

    private RequestParams getReplySubmitParams(ActionDiscussionModel model) {
        RequestParams params = RequestParams.create();
        params.putString(NewReplyDiscussionSubmitUseCase.PARAM_RESOLUTION_ID, model.getResolutionId());
        params.putString(NewReplyDiscussionSubmitUseCase.PARAM_CACHE_KEY, model.getCacheKey());
        for (AttachmentViewModel attach : model.getAttachment()) {
            if (attach.isVideo()) {
                params.putString(NewReplyDiscussionSubmitUseCase.PARAM_VIDEOS, generateVideos(model));
            } else {
                params.putString(NewReplyDiscussionSubmitUseCase.PARAM_PICTURES, generatePictures(model));
            }
        }
        return params;
    }

    private String generatePictures(ActionDiscussionModel model) {
        JsonArray json = new JsonArray();
        for (AttachmentViewModel attach : model.getAttachment()) {
            if (!attach.isVideo()) {
                json.add(attach.getUploadedFile().getPicObj());
            }
        }
        return json.toString();
    }

    private String generateVideos(ActionDiscussionModel model) {
        JsonArray json = new JsonArray();
        for (AttachmentViewModel attach : model.getAttachment()) {
            if (attach.isVideo()) {
                json.add(attach.getUploadedFile().getPicObj());
            }
        }
        return json.toString();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        this.generateHostUseCase.unsubscribe();
        this.replyDiscussionUseCase.unsubscribe();
        this.replyDiscussionSubmitUseCase.unsubscribe();
        this.uploadImageUseCase.unsubscribe();
        this.uploadVideoUseCase.unsubscribe();
    }

}
