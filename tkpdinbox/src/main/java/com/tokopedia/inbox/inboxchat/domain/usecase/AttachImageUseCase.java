package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.interactor.UploadImageUseCase;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by StevenFredian on 25/01/18.
 */

public class AttachImageUseCase extends UseCase<UploadImageDomain>{

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";

    public static final String PARAM_MESSAGE = "PARAM_MESSAGE";
    public static final String PARAM_MESSAGE_ID = "PARAM_MESSAGE_ID";
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

    private GenerateHostUseCase generateHostUseCase;
    private UploadImageUseCase uploadImageUseCase;
    private ReplyMessageUseCase replyMessageUseCase;

    public AttachImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              GenerateHostUseCase generateHostUseCase,
                              UploadImageUseCase uploadImageUseCase,
                              ReplyMessageUseCase replyMessageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.generateHostUseCase = generateHostUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
    }

    @Override
    public Observable<UploadImageDomain> createObservable(final RequestParams requestParams) {
        return Observable.just(requestParams)
                .flatMap(new Func1<RequestParams, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(RequestParams requestParams) {
                        if (isHasAttachment(requestParams))
                            return generateHost(getGenerateHostParam(), requestParams);
                        else
                            return Observable.just(requestParams);
                    }
                })
                .flatMap(new Func1<RequestParams, Observable<UploadImageDomain>>() {
                    @Override
                    public Observable<UploadImageDomain> call(RequestParams requestParams) {
                        if (isHasAttachment(requestParams))
                            return uploadFiles(getUploadFileParam(requestParams),
                                    requestParams);
                        else
                            return Observable.just(new UploadImageDomain(null, null));
                    }
                });
    }

    private RequestParams getUploadFileParam(RequestParams requestParams) {
        RequestParams params = RequestParams.create();
        params.putString(UploadImageUseCase.PARAM_SERVER_ID, requestParams.getString(PARAM_SERVER_ID, ""));
        params.putString(com.tokopedia.inbox.rescenter.detailv2.domain.interactor.UploadImageUseCase.PARAM_URL, requestParams.getString(PARAM_UPLOAD_HOST, ""));
        params.putObject(PARAM_ATTACHMENT, requestParams.getObject(PARAM_ATTACHMENT));
        return params;
    }

    private boolean isHasAttachment(RequestParams requestParams) {
        return requestParams.getObject(PARAM_ATTACHMENT) != null;
    }

    private Observable<RequestParams> generateHost(RequestParams generateHostParam, final RequestParams requestParams) {
        return generateHostUseCase.createObservable(generateHostParam)
                .flatMap(new Func1<GenerateHostDomain, Observable<RequestParams>>() {
                    @Override
                    public Observable<RequestParams> call(GenerateHostDomain generateHostModel) {
                        requestParams.putString(PARAM_SERVER_ID, generateHostModel.getServerId());
                        requestParams.putString(PARAM_UPLOAD_HOST, generateHostModel.getUploadHost());
                        return Observable.just(requestParams);
                    }
                });
    }

    private RequestParams getGenerateHostParam() {
        return GenerateHostUseCase.getParam();
    }

    private Observable<UploadImageDomain> uploadFiles(final RequestParams uploadFileParam, final RequestParams requestParams) {
        return Observable.from(getListImage(uploadFileParam))
                .flatMap(new Func1<MyChatViewModel, Observable<UploadImageDomain>>() {
                    @Override
                    public Observable<UploadImageDomain> call(MyChatViewModel imageUpload) {
                        return uploadImageUseCase.createObservable(
                                UploadImageUseCase.getParam(
                                        requestParams,
                                        requestParams.getString(PARAM_UPLOAD_HOST,""),
                                        imageUpload.getAttachment().getId(),
                                        imageUpload.getAttachment().getAttributes().getImageUrl(),
                                        requestParams.getString(PARAM_SERVER_ID,"")
                                ));
                    }
                });

    }

    private Observable<ReplyActionData> reply(RequestParams requestParams, UploadImageDomain uploadImageDomain) {
        return replyMessageUseCase.createObservable(
                ReplyMessageUseCase.generateParamAttachImage(
                        requestParams.getString(PARAM_MESSAGE_ID,"")
                        , uploadImageDomain.getPicSrc()));

    }


    private List<MyChatViewModel> getListImage(RequestParams uploadFileParam) {
        return (List<MyChatViewModel>) uploadFileParam.getObject(PARAM_ATTACHMENT);
    }


    public static RequestParams getParam(List<MyChatViewModel> attachmentList, String messageId, String userId, String deviceId) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_ATTACHMENT, attachmentList);
        params.putString(PARAM_MESSAGE_ID, messageId);
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_DEVICE_ID, deviceId);
        return params;
    }
}
