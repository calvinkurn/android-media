package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepository;
import com.tokopedia.inbox.inboxchat.data.repository.WebSocketRepository;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.ChatSocketData;

import rx.Observable;

/**
 * Created by stevenfredian on 9/12/17.
 */

public class ListenWebSocketUseCase extends UseCase<ChatSocketData>{

    private final WebSocketRepository repository;

    public ListenWebSocketUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, WebSocketRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<ChatSocketData> createObservable(RequestParams requestParams) {
        return repository.listen(requestParams.getParameters());
    }

    public static RequestParams generateParam(String messageId, String messageReply)
    {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
