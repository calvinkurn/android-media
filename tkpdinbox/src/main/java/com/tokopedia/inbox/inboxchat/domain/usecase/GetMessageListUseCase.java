package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageListUseCase extends UseCase<MessageData>{

    MessageRepository messageRepository;

    public GetMessageListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, MessageRepository messageRepository) {
        super(threadExecutor, postExecutionThread);
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<MessageData> createObservable(RequestParams requestParams) {
        return messageRepository.getMessage(requestParams.getParameters());
    }

    public static RequestParams generateParam(InboxMessagePass inboxMessagePass)
    {
        RequestParams requestParams = RequestParams.EMPTY;
        requestParams.putString("tab", "inbox");
        requestParams.putString("filter", "all");
        requestParams.putString("page", "1");
        requestParams.putString("per_page", "10");
        requestParams.putString("platform","android");
        return requestParams;
    }
}
