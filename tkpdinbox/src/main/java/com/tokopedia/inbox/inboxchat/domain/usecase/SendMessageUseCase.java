package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.MessageRepository;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import rx.Observable;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageUseCase extends UseCase<SendMessageViewModel> {

    private static final String PARAM_MESSAGE = "message";
    private static final String PARAM_TO_SHOP_ID = "to_shop_id";
    private static final String PARAM_TO_USER_ID = "to_user_id";
    private static final String PARAM_SOURCE = "source";

    private MessageRepository messageRepository;

    public SendMessageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              MessageRepository messageRepository) {
        super(threadExecutor, postExecutionThread);
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<SendMessageViewModel> createObservable(RequestParams requestParams) {
        return messageRepository.sendMessage(requestParams.getParameters());
    }


    public static RequestParams getParam(String message, String toShopId, String
            toUserId, String source) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_MESSAGE, message);
        if (toShopId != null)
            param.putString(PARAM_TO_SHOP_ID, toShopId);
        if (toUserId != null)
            param.putString(PARAM_TO_USER_ID, toUserId);
        param.putString(PARAM_SOURCE, source);
        return param;
    }
}
