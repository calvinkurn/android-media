package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageListUseCase extends UseCase<InboxChatViewModel>{

    MessageRepository messageRepository;

    public GetMessageListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, MessageRepository messageRepository) {
        super(threadExecutor, postExecutionThread);
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<InboxChatViewModel> createObservable(RequestParams requestParams) {
        return messageRepository.getMessage(requestParams.getParameters());
    }

    public static RequestParams generateParam(InboxMessagePass inboxMessagePass, int page)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("tab", "inbox");
        requestParams.putString("filter", "all");
        requestParams.putString("page", String.valueOf(page));
        requestParams.putString("per_page", "10");
        requestParams.putString("platform","android");
        return requestParams;
    }
}
