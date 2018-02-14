package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;

import rx.Observable;

/**
 * Created by stevenfredian on 9/12/17.
 */

public class ReplyMessageUseCase extends UseCase<ReplyActionData>{

    private final ReplyRepository replyRepository;

    public ReplyMessageUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ReplyRepository replyRepository) {
        super(threadExecutor, postExecutionThread);
        this.replyRepository = replyRepository;
    }

    @Override
    public Observable<ReplyActionData> createObservable(RequestParams requestParams) {
        return replyRepository.replyMessage(requestParams.getParameters());
    }

    public static RequestParams generateParam(String messageId, String messageReply)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("msg_id", messageId);
        requestParams.putString("message_reply", messageReply);
        return requestParams;
    }

    public static RequestParams generateParamAttachImage(String messageId, String filePath)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("msg_id", messageId);
        requestParams.putString("message_reply", "Uploaded Image");
        requestParams.putString("file_path", filePath);
        requestParams.putInt("attachment_type",2);
        return requestParams;
    }
}
