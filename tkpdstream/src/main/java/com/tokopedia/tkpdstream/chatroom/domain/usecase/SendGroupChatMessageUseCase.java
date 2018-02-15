package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.SendBirdException;

import javax.inject.Inject;

/**
 * @author by nisie on 2/14/18.
 */

public class SendGroupChatMessageUseCase {

    public interface SendGroupChatMessageListener {
        void onSuccessSend();

        void onErrorGetMessagesFirstTime(SendBirdException e);
    }

    @Inject
    public SendGroupChatMessageUseCase() {

    }

    public void execute(final String channelUrl,
                        String userId,
                        final SendGroupChatMessageListener listener) {

    }


}
